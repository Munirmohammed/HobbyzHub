package com.hobbyzhub.javabackend.postmodule.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.hobbyzhub.javabackend.postmodule.entity.HashTag;
import com.hobbyzhub.javabackend.postmodule.entity.Post;
import com.hobbyzhub.javabackend.postmodule.exceptions.PostNotFoundException;
import com.hobbyzhub.javabackend.postmodule.exceptions.UserAccountNotFoundException;
import com.hobbyzhub.javabackend.postmodule.repository.HashTagRepository;
import com.hobbyzhub.javabackend.postmodule.repository.PostRepository;
import com.hobbyzhub.javabackend.postmodule.requests.CreatePost;
import com.hobbyzhub.javabackend.postmodule.requests.HashTagRequest;
import com.hobbyzhub.javabackend.postmodule.responses.CreatePostResponseBody;
import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.util.def.service.AppUserServiceDef;
import com.hobbyzhub.javabackend.sharedutils.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final AppUserServiceDef appUserService;
    private final StorageService storageService;
    private final HashTagRepository hashTagRepository;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private  String accessSecret;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${application.bucket.name}")
    private String bucketName;
    private final AmazonS3 amazonS3;
    public String setId(){
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12);
    }
    public List<String> returnUrl(List<MultipartFile>files){
        List<String> names = storageService.uploadMultipleFiles(files);
        List<String> urls = new ArrayList<>();
        for(String name: names){
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis+=7*24*60*60*1000; // 7 days in milliseconds
            expiration.setTime(expTimeMillis);
            GeneratePresignedUrlRequest generatePresignedUrlRequest =new GeneratePresignedUrlRequest(bucketName,name)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration);
            urls.add(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString());
        }
        return urls;
    }
    @Modifying
    public CreatePostResponseBody createPost(CreatePost createPost,
                                             List<MultipartFile>files, String userId )  throws IOException {
        AppUser userById = appUserService.findUserById(userId);
        assert userById != null;
        log.info(userById.getFullName()+" "+userById.getEmail()+" "+userById.getBio());
        List<HashTag> hashTags = new ArrayList<>();
        List<HashTagRequest> hashTagRequests = new ArrayList<>(createPost.getHashTags());
        for(HashTagRequest hashTagRequest: hashTagRequests){
            HashTag hashTag = HashTag.builder()
                    .hashTagId(setId())
                    .tagName(hashTagRequest.getTagName())
                    .posts(null)
                    .build();
            hashTags.add(hashTag);
        }
        if(userById!=null){
            Post post = Post.builder()
                    .postId(setId())
                    .postTime(LocalDateTime.now())
                    .caption(createPost.getCaption())
                    .imageUrls(returnUrl(files))
                    .likes(Set.of())
                    .comments(Set.of())
                    .hashTags(hashTags)
                    .userId(userById.getUserId())
                    .username(userById.getFullName())
                    .profileImage(userById.getProfileImage())
                    .status(true)
                    .likeCount(0)
                    .deActivateComments(false)
                    .build();
                    Post result =postRepository.save(post);
                    List<HashTag>  hashTags1 = post.getHashTags();
            return CreatePostResponseBody.builder()
                    .postId(post.getPostId())
                    .caption(post.getCaption())
                    .postTime(post.getPostTime())
                    .profileImage(post.getProfileImage())
                    .status(post.isStatus())
                    .userId(post.getUserId())
                    .username(post.getUsername())
                    .hashTags(hashTags)
                    .comments(Set.of())
                    .likes(Set.of())
                    .imageUrls(post.getImageUrls())
                    .build();
        }else{
            throw new UserAccountNotFoundException("We couldn't resolve user of the passed userId:"+ userId);
        }
    }
    public List<Post> posts() {
        List<Post> posts = postRepository.findAll();
            if(!(posts.isEmpty())){
            return posts;
        }else{
            throw new PostNotFoundException("there are not posts inside here");
        }
    }
    public Post getPost(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(()->
                        new PostNotFoundException("Post wasn't found with passed id: "+postId));
    }
    @Modifying
    public String  deletePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->
                        new PostNotFoundException("Post wasn't found with passed id: "+postId));
        List<String> images = post.getImageUrls();
        //deleting posts should also delete comments and likes:
        //😂also another thing the tags are not deleted so added deletion of tags manually kinda😂
        List<HashTag> hashTags = post.getHashTags();
        if(!(images.isEmpty())){
            postRepository.delete(post);
            for(String str: images){
                storageService.deleteFiles(images);
            }
            for(HashTag hashTag: hashTags){
                hashTagRepository.delete(hashTag);
            }
            log.info("successfully deleted post with its images...");
            return "deleted";
        }
        log.info("couldn't delete the post neither the images...");
        return "not deleted";
    }

    public int getPostCount(String userId) {
        List<Post> userPosts = postRepository.findByUserId(userId);
        return userPosts.size();
    }

    public List<Post> getUserPosts(String userId) {
        return postRepository.findByUserId(userId);
    }

}
