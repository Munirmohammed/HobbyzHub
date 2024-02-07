package com.hobbyzhub.javabackend.postmodule.service;/*
*
@author ameda
@project backend-modulith
@package com.hobbyzhub.javabackend.postmodule.service
*
*/


import com.amazonaws.services.s3.AmazonS3;
import com.hobbyzhub.javabackend.postmodule.entity.Story;
import com.hobbyzhub.javabackend.postmodule.repository.StoryRepository;
import com.hobbyzhub.javabackend.postmodule.requests.StoryRequest;
import com.hobbyzhub.javabackend.postmodule.responses.StoryCreateResponse;
import com.hobbyzhub.javabackend.postmodule.responses.StoryViewResponse;
import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StoryService {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private  String accessSecret;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${application.bucket.name}")
    private String bucketName;
    private final AmazonS3 amazonS3;
    private final StoryRepository storyRepository;
    private final PostService postService;
    private final AppUserService appUserService;
    private final ModelMapper modelMapper;

    private String generateId() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12);
    }
    /*
    * Handles the uploading of the images for stories to the S3
    * does internal conversions based off the post-service logic implementation.
    *
    *
    * */
    @Modifying
    public StoryCreateResponse uploadStory(StoryRequest request, List<MultipartFile> files,String email){
        AppUser userByEmail = appUserService.findUserByEmail(email);
        assert  userByEmail !=null;
        /*
        * if not nul proceed and create a story for user...
        * */
        Story story = Story.builder()
                .storyId(generateId())
                .storyCaption(request.getStoryCaption())
                .storyImages(postService.returnUrl(files))
                .creationTime(formatDatesAndTime(LocalDateTime.now()))
                .username(userByEmail.getFullName())
                .deletionTime(formatDatesAndTime(RightNowDateTime()))
                .userProfileImage(userByEmail.getProfileImage())
                .email(userByEmail.getEmail())
                .expired(false)
                .duration(request.getStoryDuration())
                .remainingTime(0)
                .build();
        Story saveStory = storyRepository.save(story);
        // begin with the counter...
        log.info("remaining time since creation: "+story.getRemainingTime());
        return  StoryCreateResponse.builder()
                .storyImages(saveStory.getStoryImages())
                .createdTime(saveStory.getCreationTime())
                .userProfileImage(saveStory.getUserProfileImage())
                .username(saveStory.getUsername())
                .build();
    }
        /*
        * Below logic is for time right now
        * */
    private LocalDateTime RightNowDateTime(){
        return LocalDateTime.now();

        /*
        * Below logic is for later time when story expires which is after 24 hours
        * */
    }
    private LocalDateTime LaterDateTime(LocalDateTime rightNow){
        return RightNowDateTime().plusHours(24);
    }
    /*
    * Given that our aim is to provide the time and dates greatly and presentable
    * Therefore we provide source formatting for it to be readable.
    * */
    private String formatDatesAndTime(LocalDateTime obj){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(obj);
    }
    private int  remainingTimeHour(LocalDateTime thenTime){
        return thenTime.getHour() - RightNowDateTime().getHour();
    }

    public List<StoryViewResponse> displayStories() {
        List<Story> all = storyRepository.findAll();
        List<StoryViewResponse> viewResponses = new ArrayList<>();
        for(Story story: all){
            viewResponses.add(modelMapper.map(story,StoryViewResponse.class));
        }
        return viewResponses;
    }

    public AppUser getUserByEmail(String email) {
        return appUserService.findUserByEmail(email);
    }

    public List<Story> retrieveStoriesPerUser(String email) {
        return  storyRepository.findByEmail(email);
    }
}
