package com.hobbyzhub.javabackend.postmodule.controller;

import com.hobbyzhub.javabackend.postmodule.entity.HashTag;
import com.hobbyzhub.javabackend.postmodule.requests.CreateTagRequest;
import com.hobbyzhub.javabackend.postmodule.responses.PostGenericResponse;
import com.hobbyzhub.javabackend.postmodule.service.HashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
public class HashTagController {
    private final HashTagService hashTagService;
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;
    @PostMapping("/create-tag")
    public ResponseEntity<?> createTag(@RequestBody CreateTagRequest createTagRequest,
                                       @RequestParam String postId){
        HashTag hashTag =  hashTagService.createTag(createTagRequest,postId);
        if(hashTag!=null){
            return new ResponseEntity<>( new PostGenericResponse<>(
                    apiVersion,
                    organizationName,
                    "tag created successfully...",
                    hashTag
            ),HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>( new PostGenericResponse<>(
                    apiVersion,
                    organizationName,
                    "tag wasn't created, pls try again",
                    null
            ),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> allTags(){
        return new ResponseEntity<>(new PostGenericResponse<>(
                apiVersion,
                organizationName,
                "successful retrieval of all tags",
                hashTagService.allTags()
        ),HttpStatus.OK);
    }
}
