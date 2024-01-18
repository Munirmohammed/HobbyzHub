package com.hobbyzhub.javabackend.securitymodule.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.hobbyzhub.javabackend.mailingmodule.InjectableAccountsMailingComponent;
import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.repository.AppUserRepository;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.hobbyzhub.javabackend.securitymodule.types.Gender;
import com.hobbyzhub.javabackend.securitymodule.util.def.service.AppUserServiceDef;
import com.hobbyzhub.javabackend.sharedexceptions.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class AppUserService implements AppUserServiceDef {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private StorageService storageService;

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InjectableAccountsMailingComponent injectableAccountsMailingComponent;

    @Override
    public void registerNewAccount(AppUser appUser) {
        appUser.setJoinedDate(generateJoinedDate());
        appUserRepository.save(appUser);
    }

    @Override
    public AppUser findUserById(String userId) {
        return appUserRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User by id " + userId + " not found")
        );
    }



    @Override
    public void sendVerificationEmail(String email, String intent) throws MessagingException, EntityNotFoundException {
        int otpNumber = new java.util.Random().nextInt((9999 - 1000) + 1) + 1000;
        AppUser appUser = appUserRepository.findUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User by email " + email + " not found")
        );
        appUser.setTemporaryOtp(otpNumber);
        appUserRepository.save(appUser);

        switch (intent) {
            case "verify-email" -> {
                if(injectableAccountsMailingComponent.sendEmailVerificationMail(email, String.valueOf(otpNumber))
                        .getStatusCode().value() != 200) {
                    throw new MessagingException("Unknown error while sending email");
                }
            }

            case "reset-password" -> {
                if(injectableAccountsMailingComponent.sendPasswordResetVerificationMail(email, String.valueOf(otpNumber))
                        .getStatusCode().value() != 200) {
                    throw new MessagingException("Unknown error while sending email");
                }
            }

            default -> {
                throw new MessagingException("Invalid request structure.");
            }
        }
    }

    @Override
    public List<AppUser> getAccountsList(int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(AppUser.class)));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<AppUser> criteriaQuery = criteriaBuilder.createQuery(AppUser.class);
        Root<AppUser> from = criteriaQuery.from(AppUser.class);
        CriteriaQuery<AppUser> select = criteriaQuery.select(from);

        TypedQuery<AppUser> typedQuery = entityManager.createQuery(select);

        while(pageNumber < count.intValue()) {
            typedQuery.setFirstResult(pageNumber - 1);
            typedQuery.setMaxResults(pageSize);
            pageNumber += pageSize;
        }

        return typedQuery.getResultList();

    }

    @Override
    public AppUser updateUserDetails(String userId, String fullName, String birthdate, String gender, String bio, MultipartFile profileImage) {
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User by id " + userId + " not found")
        );
        appUser.setFullName(fullName);
        appUser.setBirthdate(LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        appUser.setBio(bio);

        // set gender
        switch(gender) {
            case "male" -> {
                appUser.setGender(Gender.MALE);
            }

            case "female" -> {
                appUser.setGender(Gender.FEMALE);
            }

            case "other" -> {
                appUser.setGender(Gender.OTHER);
            }
        }

        // delegate profile image
        if(Objects.isNull(profileImage)) {
            appUserRepository.save(appUser);
        } else {
            String fileUrl = this.generateFileUrl(profileImage);
            System.out.println("File url: " + fileUrl);
            appUser.setProfileImage(this.generateFileUrl(profileImage));
        }

        return appUserRepository.save(appUser);
    }

    @Override
    public void verifyOTP(String email, Integer otp) {
        AppUser appUser = appUserRepository.findUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User by email " + email + " not found")
        );

        // check OTP
        if(!Objects.equals(appUser.getTemporaryOtp(), otp)) {
            // TODO: throw custom exception here
            throw new RuntimeException("Exception message here");
        }

        appUser.setTemporaryOtp(null);
        appUserRepository.save(appUser);
    }

    @Override
    public void deleteAccountByEmail(String email) {
        AppUser appUser = appUserRepository.findUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User by email " + email + " not found")
        );
        appUserRepository.delete(appUser);
    }

    private LocalDate generateJoinedDate() {
        // get the LocalDate now
        LocalDate dateNow = LocalDate.now();

        //create the pattern we want to use
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // format dd-MM-yyyy
        String formattedDate = dateNow.format(dateFormat);
        return LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @Override
    public AppUser createUserAccount(AppUser appUser) throws EntityExistsException {
        if(appUserRepository.existsByEmail(appUser.getEmail())) {
            log.warn("Attempt at registering with existing email");
            throw new EntityExistsException("Account for that email already exists");
        }

        String userId = generateId();
        appUser.setUserId(userId);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setNewAccount(true);
        appUser.setAccountActive(false);

        // save in database
        appUser = appUserRepository.save(appUser);

        
        return appUser;
    }

    @Override
    public void markAccountNotNew(String userId) {
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Account by id " + userId + " not found"));
        appUser.setNewAccount(false);
        appUserRepository.save(appUser);
    }

    @Override
    public void activateAccount(String email) {
        AppUser appUser = appUserRepository.findUserByEmail(email).orElseThrow(
            () -> new EntityNotFoundException("Account by email " + email + " not found"));

        appUser.setAccountActive(true);
        appUserRepository.save(appUser);
    }

    @Override
    public void resetPassword(AppUser appUser) {
        AppUser existingCredentials = appUserRepository.findUserByEmail(appUser.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Account by email " + appUser.getEmail() + " not found"));
        existingCredentials.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(existingCredentials);
    }

    private String generateId() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12);
    }

    private String generateFileUrl(MultipartFile file) {
        String name = storageService.uploadPicture(file);
        String url = "";

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += (7 * 24 * 60 * 60 * 1000);
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(bucketName, name)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        url = s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        return url;
    }
}