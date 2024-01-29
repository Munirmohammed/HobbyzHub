package com.hobbyzhub.javabackend.categoriesmodule.abstractions;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.hobbyzhub.javabackend.sharedutils.StorageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public abstract class AbstractEntityHelperDef<T> {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private StorageService storageService;

    protected final String generateRandomUUID() {
        return UUID.randomUUID()
            .toString()
            .replace("-", "")
            .substring(0, 12);
    }

    protected final String convertNameToTitleCase(String uncasedString) {
        char[] delimiters = { ' ' };
        return WordUtils.capitalizeFully(uncasedString, delimiters);
    }

    protected final List<T> retrievePagedList(int pageNumber, int pageSize, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(clazz)));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> from = criteriaQuery.from(clazz);
        CriteriaQuery<T> select = criteriaQuery.select(from);

        TypedQuery<T> typedQuery = entityManager.createQuery(select);

        while(pageNumber < count.intValue()) {
            typedQuery.setFirstResult(pageNumber - 1);
            typedQuery.setMaxResults(pageSize);
            pageNumber += pageSize;
        }

        return typedQuery.getResultList();
    }

    protected final String generateFileUrl(MultipartFile file) {
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
