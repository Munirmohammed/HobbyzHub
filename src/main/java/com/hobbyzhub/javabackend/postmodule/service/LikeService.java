package com.hobbyzhub.javabackend.postmodule.service;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.hobbyzhub.javabackend.notificationsmodule.dto.MessageDTO;
import com.hobbyzhub.javabackend.notificationsmodule.service.FcmService;
import com.hobbyzhub.javabackend.postmodule.entity.Like;
import com.hobbyzhub.javabackend.postmodule.entity.Post;
import com.hobbyzhub.javabackend.postmodule.exceptions.LikeNotFoundException;
import com.hobbyzhub.javabackend.postmodule.exceptions.PostNotFoundException;
import com.hobbyzhub.javabackend.postmodule.repository.LikeRepository;
import com.hobbyzhub.javabackend.postmodule.repository.PostRepository;
import com.hobbyzhub.javabackend.securitymodule.SharedAccounts;
import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final FcmService fcmService;

    @Autowired
    private SharedAccounts accountManagement;

    private int counter = 1;
    public String setId(){
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12);
    }

    @Modifying
    public Like createLike(String postId,String likerUserId) {
        Post post = postRepository.findById(postId).get();
        Like like = null;
        String userIdOfPost = post.getUserId();
        String userIdOfLiker;
        if(post.getPostId().equalsIgnoreCase(postId) && likerUserId!=null){
            like = Like.builder()
                    .likeId(setId())
                    .likeCount(counter++)
                    .postLike(post)
                    .profileImage(post.getProfileImage())
                    .username(post.getUsername())
                    .likeTime(LocalDateTime.now())
                    .userId(likerUserId)
                    .build();
            log.info("successfully placed a like...");
              like = likeRepository.save(like);

            // Send notification to the post creator
            sendLikeNotification(post);

              //after successfully saving, update the like count in post table...
            post.setLikeCount(like.getLikeCount());
              return like;
        }else{
            log.info("like couldn't be placed...");
            throw new PostNotFoundException("post was not found, therefore like is disabled.");
        }
    }

    private void sendLikeNotification(Post post) {
        String postCreatorUserId = post.getUserId();
        SharedAccountsInformation sharedInfo = accountManagement.retrieveSharedAccount(postCreatorUserId);
        String firebaseToken = sharedInfo.getFirebaseToken();

        List<String> imageUrls = post.getImageUrls();
        log.info(String.valueOf(imageUrls));

        String imageUrl = null;
        if (!imageUrls.isEmpty()) {
            imageUrl = imageUrls.get(0); // Get the first URL if available
        }
        // Check if the Firebase token is not null before sending the notification
        if (firebaseToken != null) {
            try {
                Map<String, String> data = new HashMap<>();
                data.put("postId", post.getPostId());
                MessageDTO notificationMessage = new MessageDTO(
                        "Your post has been liked",
                        "One of your posts has received a like",
                        data,
                        imageUrl,
                        firebaseToken
                );

                // Send the notification
                try {
                    fcmService.sendNotificationToSpecificDevice(notificationMessage, firebaseToken);
                    log.info("Notification sent to post creator for the liked post: {}", post.getPostId());
                } catch (FirebaseMessagingException e) {
                    // Handle any errors that occur during notification sending
                    log.error("Error sending notification for liked post: {}", post.getPostId(), e);
                }
            } catch (Exception e) {
                // Handle any unexpected errors
                log.error("Error preparing notification for liked post: {}", post.getPostId(), e);
            }
        } else {
            log.warn("Firebase token is null for user: {}", postCreatorUserId);
            // notificationQueueService.queueNotification(new Notification(postCreatorUserId, notificationMessage));
        }
    }



//    private MessageDTO getMessageDTO(Post post, SharedAccountsInformation sharedInfo) {
//        List<String> imageUrls = post.getImageUrls();
//        String token = sharedInfo.getFirebaseToken();
//
//
//        String imageUrl = null;
//        if (!imageUrls.isEmpty()) {
//            imageUrl = imageUrls.get(0); // Get the first URL if available
//        }
//
//        // Create the notification message
//        MessageDTO messageDTO = new MessageDTO(
//                "Your post has been liked",
//                "One of your posts has received a like",
//                null, // additional data if needed
//                imageUrl, // image URL if available
//                token// Pass the Firebase token or user ID of the post creator
//        );
//        // Log the MessageDTO
//        log.info("Created MessageDTO: {}", messageDTO);
//        return messageDTO;
//    }


    public List<Like> displayLikes() {
        List<Like> likes = likeRepository.findAll();
        if(!(likes.isEmpty())){
            return likes;
        }else{
            throw new LikeNotFoundException("likes not found.");
        }
    }

    public Map<String,Boolean> unlike(String likeId) {
        Like like = likeRepository.findById(likeId)
                .orElseThrow(
                        ()-> new LikeNotFoundException("Like with passed Id not found"+likeId));
        Post post = like.getPostLike();
        if(like.getLikeCount()>0 && post!=null){
            like.setLikeCount(like.getLikeCount()-1);
            likeRepository.save(like);
            //also, go head and deduct from post table by 1...
            post.setLikeCount(like.getLikeCount());
            log.info("successfully unliked post: "+like.getPostLike().getPostId());
        }else{
            log.info("unliking operation was unsuccessful.");
        }
        Map<String,Boolean> response = new HashMap<>();
        response.put("unliked",true);
        return response;
    }
}
