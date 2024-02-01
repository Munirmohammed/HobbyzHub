package com.hobbyzhub.javabackend.sharedutils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadPicture(MultipartFile multipartFile) {
        File file = convertMultipartFileToFile(multipartFile);
        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
        file.delete();
        return fileName;
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException ex) {
            log.error("Error converting MultipartFile to file", ex);
        }
        return convertedFile;
    }
    public List<String> uploadMultipleFiles(List<MultipartFile> files){
        List<String> uploadedFiles =  new ArrayList<>(files.size());
        //check if the file formats are okay...
        for(MultipartFile file: files){
            String str = uploadPicture(file);
            uploadedFiles.add(str);
        }
        return uploadedFiles;
    }
    public byte [] downloadFile(String fileName){
        S3Object s3Object = s3Client.getObject(bucketName,fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try{
//            byte [] content = IOUtils.toByteArray(inputStream);
//            return content;
            return IOUtils.toByteArray(inputStream);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }
    public int deleteFiles(List<String> fileNames){
        List<String> result =new ArrayList<>(fileNames);
        int counter = fileNames.size();
        for(String str: result){
            deleteFile(str);
            counter--;
        }
        return counter;
    }
    public List<String> listAllFiles(){
        ListObjectsV2Result listObjectsV2Result = s3Client.listObjectsV2(bucketName);
        return listObjectsV2Result.getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }
    private boolean isValidFileType(MultipartFile multipartFile){
        String contentType = multipartFile.getContentType();
        return false;
    }
}


