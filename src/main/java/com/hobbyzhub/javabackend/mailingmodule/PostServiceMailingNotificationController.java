package com.hobbyzhub.javabackend.mailingmodule;

import com.hobbyzhub.javabackend.mailingmodule.service.MailingService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class PostServiceMailingNotificationController {
//    private final MailingService mailingService;
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    public PostServiceMailingNotificationController(MailingService mailingService) {
//        this.mailingService = mailingService;
//    }
//    @PutMapping(value = "/mail/notification/add-post/{userEmail}")
//    public ResponseEntity<?> sendPostAddedNotification(@PathVariable("userEmail") String userEmail){
//        try {
//            mailingService.sendPostAddedNotification(userEmail);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }catch (MessagingException ex){
//            logger.error("Couldn't send notification to given address.");
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    @PutMapping(value = "/mail/notification/view-post/{userEmail}")
//    public ResponseEntity<?> viewPostNotification(@PathVariable("userEmail") String userEmail){
//        try{
//            mailingService.viewPostNotification(userEmail);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }catch (MessagingException ex){
//            logger.error("Couldn't send notification to given address.");
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    @PutMapping(value = "/mail/notification/delete-post-type/{userEmail}")
//    public ResponseEntity<?> deletePostTypeNotification(@PathVariable("userEmail") String userEmail){
//        try{
//            mailingService.deletePostTypeNotification(userEmail);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }catch (MessagingException ex){
//            logger.error("Couldn't send notification to given address.");
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    @PutMapping(value = "/mail/notification/add-post-type/{userEmail}")
//    public ResponseEntity<?> addPostTypeNotification(@PathVariable("userEmail") String userEmail){
//        try{
//            mailingService.addPostTypeNotification(userEmail);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }catch (MessagingException ex){
//            logger.info("Couldn't send notification to given address.");
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    @PutMapping(value = "/mail/notification/get-post-type/{userEmail}")
//    public ResponseEntity<?> getPostTypeNotification(@PathVariable("userEmail") String userEmail){
//        try{
//            mailingService.getPostTypeNotification(userEmail);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }catch (MessagingException ex){
//            logger.info("Couldn't send notification to given address.");
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    @PutMapping(value = "/mail/notification/get-post-type/{userEmail}/{typeId}")
//    public ResponseEntity<?> deletePostTypeNotification(@PathVariable("userEmail") String userEmail,
//                                                        @PathVariable("typeId") String typeId){
//        try{
//            mailingService.deletePostTypeNotification(userEmail,typeId);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }catch (MessagingException ex){
//            logger.info("Couldn't send notification to given address.");
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
