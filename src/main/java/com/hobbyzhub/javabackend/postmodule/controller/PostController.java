package com.hobbyzhub.javabackend.postmodule.controller;
import com.hobbyzhub.javabackend.postmodule.entity.Post;
import com.hobbyzhub.javabackend.postmodule.requests.CreatePost;
import com.hobbyzhub.javabackend.postmodule.requests.UserIdRequest;
import com.hobbyzhub.javabackend.postmodule.responses.CreatePostResponseBody;
import com.hobbyzhub.javabackend.postmodule.responses.GetAllPostsResponse;
import com.hobbyzhub.javabackend.postmodule.responses.PostGenericResponse;
import com.hobbyzhub.javabackend.postmodule.service.PostService;
import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.util.def.service.AppUserServiceDef;
import com.hobbyzhub.javabackend.sharedconfig.S3Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Slf4j
public class PostController {
    private final PostService postService;
    private final S3Configuration s3Configuration;
    private final AppUserServiceDef appUserService;
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;
    @PostMapping(value = "/upload/{userId}",consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> createPost(@RequestPart CreatePost createPost,
                                        @RequestPart("files") List<MultipartFile> files,
                                        @PathVariable("userId") String userId) throws IOException {

        CreatePostResponseBody createPostResponseBody = postService.createPost(createPost,files,userId);
        if(createPostResponseBody!=null){
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "successfully created a post...",
                            createPostResponseBody
                    )
                    ,HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "post creation request failed...",
                            null
                    )
                    ,HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> posts(){
        List<Post> posts = postService.posts();
        List<GetAllPostsResponse> result = new ArrayList<>();
        for(Post post: posts){
            GetAllPostsResponse body = GetAllPostsResponse.builder()
                    .postId(post.getPostId())
                    .caption(post.getCaption())
                    .postTime(post.getPostTime())
                    .profileImage(post.getProfileImage())
                    .status(post.isStatus())
                    .userId(post.getUserId())
                    .username(post.getUsername())
                    .hashTags(post.getHashTags())
                    .imageUrls(post.getImageUrls())
                    .likes(post.getLikes())
                    .comments(post.getComments())
                    .build();
            result.add(body);
        }
        if(!(result.isEmpty())){
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "posts successfully retrieved",
                            result
                    )
                    ,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "posts not retrieved",
                            null
                    )
                    ,HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getPost(@PathVariable("postId") String postId){
       Post post = postService.getPost(postId);
        GetAllPostsResponse body = GetAllPostsResponse.builder()
                .postId(post.getPostId())
                .caption(post.getCaption())
                .postTime(post.getPostTime())
                .profileImage(post.getProfileImage())
                .status(post.isStatus())
                .username(post.getUsername())
                .userId(post.getUserId())
                .hashTags(post.getHashTags())
                .imageUrls(post.getImageUrls())
                .likes(post.getLikes())
                .comments(post.getComments())
                .build();
        return new ResponseEntity<>(
                new PostGenericResponse<>(
                        apiVersion,
                        organizationName,
                        "successfully displayed requested post...",
                        post
                )
                , HttpStatus.OK);
    }
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") String postId){
        if(postService.deletePost(postId).equalsIgnoreCase("deleted")){
            return new ResponseEntity<>(new PostGenericResponse<>(
                    apiVersion,
                    organizationName,
                    "successfully deleted a post.",
                    "deleted"
            ), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new PostGenericResponse<>(
                    apiVersion,
                    organizationName,
                    "post is not deleted.",
                    "not deleted."
            ),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/accounts-data/{userId}")
    public ResponseEntity<?> getAccountsData(@PathVariable("userId") String userId){
        AppUser appUser =  appUserService.findUserById(userId);
        return new ResponseEntity<>(appUser,HttpStatus.OK);
    }

    /**
     * Retrieves the count of posts for a given user identified by the provided userId.
     * This endpoint is designed to return the number of posts associated with a user.
     * If the operation is successful, it responds with the post count and a status of OK (200).
     * If any error occurs during the process, an appropriate status code and error message will be returned.
     * <p>
     * Note: This API is implemented by Munir Mohammed. Do not modify this code without proper consultation,
     * as there may arise issues related to working on this functionality.
     *
     * @param userId The unique identifier of the user for whom the post count is requested.
     * @return ResponseEntity<Integer> containing the post count and the HTTP status.
     */

    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> getPostCount(@PathVariable("userId") String userId) {
        int postCount = postService.getPostCount(userId);
        return new ResponseEntity<>(postCount, HttpStatus.OK);
    }

    /**
     * Retrieves all the posts for a given user identified by the provided userId.
     * This endpoint is designed to return the all of the posts associated with that user.
     * If the operation is successful, it responds with list of posts and a status of OK (200).
     * If any error occurs during the process, an appropriate status code and error message will be returned.
     * <p>
     * Note: This API is implemented by Munir Mohammed. Do not modify this code without proper consultation,
     * as there may arise issues related to working on this functionality.
     */
    @PostMapping("/user-posts")
    public ResponseEntity<?> getUserPosts(@RequestBody UserIdRequest userIdRequest) {
        // UserIdRequest is a class to represent the request body containing userId
        String userId = userIdRequest.getUserId();

        List<Post> userPosts = postService.getUserPosts(userId);

        List<GetAllPostsResponse> result = new ArrayList<>();
        for (Post post : userPosts) {
            GetAllPostsResponse body = GetAllPostsResponse.builder()
                    .postId(post.getPostId())
                    .caption(post.getCaption())
                    .postTime(post.getPostTime())
                    .profileImage(post.getProfileImage())
                    .status(post.isStatus())
                    .userId(post.getUserId())
                    .username(post.getUsername())
                    .hashTags(post.getHashTags())
                    .imageUrls(post.getImageUrls())
                    .likes(post.getLikes())
                    .comments(post.getComments())
                    .build();
            result.add(body);
        }

        if (!result.isEmpty()) {
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "User posts successfully retrieved",
                            result
                    ),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new PostGenericResponse<>(
                            apiVersion,
                            organizationName,
                            "User posts not retrieved",
                            null
                    ),
                    HttpStatus.NOT_FOUND);
        }
    }
}

