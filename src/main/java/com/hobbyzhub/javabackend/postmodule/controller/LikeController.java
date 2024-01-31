package com.hobbyzhub.javabackend.postmodule.controller;


import com.hobbyzhub.javabackend.postmodule.entity.Like;
import com.hobbyzhub.javabackend.postmodule.responses.PostGenericResponse;
import com.hobbyzhub.javabackend.postmodule.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;
    @PostMapping("/create")
    public ResponseEntity<?> createLike(@RequestParam String postId){
        Like like = likeService.createLike(postId);
        if(like!=null){
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "successfully initiated a like...",
                            like
                    )
                    ,HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "couldn't initiate a like",
                            null
                    )
                    ,HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("/display/all")
    public ResponseEntity<?> displayLikes(){
        List<Like> likes = likeService.displayLikes();
        if(likes.isEmpty()){
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "couldn't display list of likes...",
                            null
                    )
                    ,HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "successfully displaying list of likes",
                            likes
                    )
                    ,HttpStatus.OK);
        }
    }
    @GetMapping("/unlike")
    public ResponseEntity<?> unlike(@RequestParam("likeId") String likeId){
          Map<String,Boolean> result =likeService.unlike(likeId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}