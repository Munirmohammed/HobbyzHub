package com.hobbyzhub.javabackend.securitymodule.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.hobbyzhub.javabackend.mailingmodule.InjectableAccountsMailingComponent;
import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.entity.UserRoles;
import com.hobbyzhub.javabackend.securitymodule.repository.AppUserRepository;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.hobbyzhub.javabackend.securitymodule.types.Gender;
import com.hobbyzhub.javabackend.securitymodule.util.def.service.AppUserServiceDef;
import com.hobbyzhub.javabackend.sharedexceptions.MessagingException;
import com.hobbyzhub.javabackend.sharedexceptions.ServerErrorException;
import com.hobbyzhub.javabackend.sharedutils.StorageService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.*;

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

        // change newAccount value to false
        appUser.setNewAccount(false);

        // delegate profile image
        if(Objects.isNull(profileImage)) {
            appUserRepository.save(appUser);
        } else {
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
    public void saveUser(AppUser appUser) {
        try {
            appUserRepository.save(appUser);
        } catch (Exception e) {
            log.error("Error saving user", e);
            throw new ServerErrorException("Error saving user");
        }
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
        appUser.setJoinedDate(LocalDate.now());
        appUser.setRoles(Collections.singleton(UserRoles.ROLE_USER));
        /*
        * adding default role to user as ROLE_USER
        * */
        // save in database
        appUser = appUserRepository.save(appUser);
        return appUser;
    }

    @Override
    public AppUser createAdminAccount(AppUser newAppUser) {
        if(appUserRepository.existsByEmail(newAppUser.getEmail())) {
            log.warn("Attempt at registering with existing email");
            throw new EntityExistsException("Account for that email already exists");
        }
        String userId = generateId();
        newAppUser.setUserId(userId);
        newAppUser.setPassword(passwordEncoder.encode(newAppUser.getPassword()));
        newAppUser.setNewAccount(true);
        newAppUser.setAccountActive(false);
        newAppUser.setJoinedDate(LocalDate.now());
        newAppUser.setRoles(Collections.singleton(UserRoles.ROLE_ADMIN));
        /*
         * adding default role to user as ROLE_ADMIN
         * */
        // save in database
        newAppUser = appUserRepository.save(newAppUser);
        return newAppUser;
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

    @Override
    public List<AppUser> searchUsersByName(String searchSlug, Integer page, Integer size) {
        Pageable pageInfo = PageRequest.of(page, size);
        return appUserRepository.findByFullName(searchSlug, pageInfo).stream().parallel().peek(appUser -> appUser.setPassword(null)).toList();
    }

    /*
     *  @utility method
     * @author ameda
     * */

    @Override
    public AppUser findUserByEmail(String email) {
        return  appUserRepository.findUserByEmail(email).orElseThrow(
                ()-> new EntityNotFoundException("specified entity could not be established..."));
    }

    private String generateId() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12);
    }

    private String generateFileUrl(MultipartFile file) {
        String name = storageService.uploadFile(file);
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

    @Override
    public void saveFirebaseToken(String userId, String firebaseToken) {
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User by id " + userId + " not found")
        );
        appUser.setFirebaseToken(firebaseToken);
        appUserRepository.save(appUser);
    }

    @Override
    public void deleteFirebaseToken(String userId) {
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User by id " + userId + " not found")
        );
        appUser.setFirebaseToken(null);
        appUserRepository.save(appUser);
    }
}
