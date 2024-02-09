package com.hobbyzhub.javabackend.chatsmodule.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.hobbyzhub.javabackend.sharedutils.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class ChatMediaService {
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private StorageService storageService;

    @Value("${application.bucket.name}")
    private String bucketName;

    public String generateFileUrl(MultipartFile file) {
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
}
