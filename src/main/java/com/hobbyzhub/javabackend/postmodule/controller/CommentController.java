package com.hobbyzhub.javabackend.postmodule.controller;

import com.hobbyzhub.javabackend.postmodule.entity.Comment;
import com.hobbyzhub.javabackend.postmodule.exceptions.CommentNotFoundException;
import com.hobbyzhub.javabackend.postmodule.requests.CreateCommentRequest;
import com.hobbyzhub.javabackend.postmodule.responses.CreateCommentResponse;
import com.hobbyzhub.javabackend.postmodule.responses.PostGenericResponse;
import com.hobbyzhub.javabackend.postmodule.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;
    @PostMapping("/comment/create")
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest createCommentRequest,
                                           @RequestParam String postId){
        Comment comment = commentService.createComment(createCommentRequest,postId);
        CreateCommentResponse response = CreateCommentResponse.builder()
                .commentId(comment.getCommentId())
                .commentCount(comment.getCommentCount())
                .message(comment.getMessage())
                .like(comment.getLike())
                .username(comment.getUsername())
                .profileImage(comment.getProfileImage())
                .commentTime(LocalDateTime.now())
                .build();
        if(comment!=null){
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "comment created successfully...",
                            comment
                    )
                    ,HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "comment couldn't be created...",
                            null
                    )
                    ,HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<?> getCommentByCommentId(@PathVariable("commentId") String commentId){
        Comment comment = commentService.getComment(commentId);
        if(comment!=null){
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "successfully retrieved comment with passed comment Id",
                            comment
                    )
                    ,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "comment couldn't be retrieved, wrong commentId provided",
                            null
                    )
                    ,HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/comment/all")
    public ResponseEntity<?> comments() throws CommentNotFoundException {
        List<Comment> commentList = commentService.comments();
        if(commentList.isEmpty()){
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "couldn't retrieve posts...",
                            null
                    )
                    ,HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "successfully displaying list of comments",
                            commentList
                    )
                    ,HttpStatus.OK);
        }
    }
}
