package com.hobbyzhub.javabackend.postmodule.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.hobbyzhub.javabackend.notificationsmodule.dto.MessageDTO;
import com.hobbyzhub.javabackend.notificationsmodule.service.FcmService;
import com.hobbyzhub.javabackend.postmodule.entity.Comment;
import com.hobbyzhub.javabackend.postmodule.entity.Post;
import com.hobbyzhub.javabackend.postmodule.exceptions.CommentNotFoundException;
import com.hobbyzhub.javabackend.postmodule.repository.CommentRepository;
import com.hobbyzhub.javabackend.postmodule.repository.PostRepository;
import com.hobbyzhub.javabackend.postmodule.requests.CreateCommentRequest;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final FcmService fcmService;

    @Autowired
    private SharedAccounts accountManagement;

    /*
    * counter variable shouldn't be made final as
    * it keeps on updating the count
    **/
    private int counter=1;
    public String setId(){
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12);
    }

    @Modifying
    public Comment createComment(CreateCommentRequest createCommentRequest,
                                 String postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        Comment comment = null;
        comment = Comment.builder()
                .commentId(setId())
                .commentCount(counter++)
                .postComment(post)
                .message(createCommentRequest.getMessage())
                .username(post.getUsername())
                .profileImage(post.getProfileImage())
                .commentTime(LocalDateTime.now())
                .deActivateComments(false)
                .build();
        assert comment != null;
        comment = commentRepository.save(comment);

        // Send notification to the post creator
        sendCommentNotification(post, comment);
        log.info(String.valueOf(comment));

        return comment;
    }

    private void sendCommentNotification(Post post, Comment comment) {
        String postCreatorUserId = post.getUserId();
        SharedAccountsInformation sharedInfo = accountManagement.retrieveSharedAccount(postCreatorUserId);
        String firebaseToken = sharedInfo.getFirebaseToken();

        if (firebaseToken != null) {
            try {
                MessageDTO notificationMessage = getMessageDTO(firebaseToken, comment);

                // Initialize the data field if not already initialized
                if (notificationMessage.getData() == null) {
                    notificationMessage.setData(new HashMap<>());
                }

                // Send the notification
                fcmService.sendNotificationToSpecificDevice(notificationMessage, firebaseToken);
                log.info("Notification sent to post creator for the commented post: {}", post.getPostId());
            } catch (IllegalArgumentException e) {
                log.error("Error sending notification for commented post due to null data in message DTO: {}", post.getPostId(), e);
            } catch (FirebaseMessagingException e) {
                log.error("Error sending notification for commented post: {}", post.getPostId(), e);
            }
        } else {
            log.warn("Firebase token is null for user: {}", postCreatorUserId);
        }
    }



    private static MessageDTO getMessageDTO(String firebaseToken, Comment comment) {
        // Prepare the notification message with comment data
        HashMap<String, String> commentData = getStringStringHashMap(comment);

        MessageDTO notificationMessage = new MessageDTO(
                "Somemone Commented on Your Post",
                comment.getMessage(),
                commentData,
                null,
                firebaseToken
        );

        if (notificationMessage.getData() == null) {
            notificationMessage.setData(new HashMap<>());
        }
        return notificationMessage;
    }


    private static HashMap<String, String> getStringStringHashMap(Comment comment) {
        HashMap<String, String> commentData = new HashMap<>();
        commentData.put("commentId", comment.getCommentId());
        commentData.put("postId", comment.getPostComment().getPostId());
        commentData.put("message", comment.getMessage());
        commentData.put("username", comment.getUsername());
        commentData.put("profileImage", comment.getProfileImage());
        commentData.put("commentTime", comment.getCommentTime().toString());
        return commentData;
    }


    public List<Comment> comments() throws CommentNotFoundException {
        List<Comment> comments = commentRepository.findAll();
        if(!(comments.isEmpty())){
            return comments;
        }else{
            throw new CommentNotFoundException("no comments are present here!");
        }
    }

    public Comment getComment(String commentId) {
        return commentRepository.findById(commentId).orElseThrow();
    }
}
