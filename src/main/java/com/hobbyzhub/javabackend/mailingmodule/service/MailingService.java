package com.hobbyzhub.javabackend.mailingmodule.service;

import com.hobbyzhub.javabackend.mailingmodule.definition.MailDefinition;
import com.hobbyzhub.javabackend.mailingmodule.wrapper.SystemStateChangeMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MailingService extends MailDefinition {
    @Autowired
    private JavaMailSender javaMailSender;
//    private final RestTemplate restTemplate;
    private final String BASE_URL="http://localhost:8701/api/";

//    public MailingService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    public void sendEmailVerificationMail(String userEmail, String otpNumber) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String messageBody = """
                             <h3 style="font-size: 18px;">Account Verification Code</h3> <br />
                             <p>
                                Hi, we have received a request to activate your email for one of your new accounts. If the action was initiated by you,
                                kindly use the code below as your activation code. Otherwise, kindly ignore this email.
                             </p>
                             <p style="font-size: 20px; font-weight: bold;">""" + otpNumber + """
                             </p>
                             """;
        String htmlMessage = generateMailTemplate(messageBody);
        
        helper.setText(htmlMessage, true);
        helper.setTo(userEmail);
        helper.setSubject("Account Verification Request");
        helper.setFrom("martbikathi@gmail.com");
        
        javaMailSender.send(mimeMessage);
        log.info("Mail Verification Email message successfully sent... ");
        
    }
    
    public void sendPasswordResetMail(String userEmail, String otpNumber) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String messageBody = """
                             <h3 style="font-size: 18px;">Password Reset Verification Code</h3> <br />
                              <p>
                                 Hi, It seems you are attempting to change your account password. If the action was initiated by you,
                                 kindly use the code below as your verification code before you can proceed. 
                                 Otherwise, kindly ignore this email.
                              </p>
                              <p style="font-size: 20px; font-weight: bold;">""" + otpNumber + """
                              </p>
                             """;
        String htmlMessage = generateMailTemplate(messageBody);
        
        helper.setText(htmlMessage, true);
        helper.setTo(userEmail);
        helper.setSubject("Password Reset Verification Code");
        helper.setFrom("martbikathi@gmail.com");
        
        javaMailSender.send(mimeMessage);
        log.info("Password Reset Email message successfully sent... ");
    }
                /* POST SERVICE NOTIFICATIONS */
//    public void sendPostAddedNotification(String userEmail) throws MessagingException{
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//        SystemStateChangeMessage systemStateChangeMessage=restTemplate
//                .getForObject(BASE_URL+"/notification/add-post", SystemStateChangeMessage.class);
//        log.info("Talking to notification service for message body for add -post endpoint.");
//        helper.setText(systemStateChangeMessage.getMessage(),true);
//        helper.setTo(systemStateChangeMessage.getUserId());
//        helper.setSubject("add-post alert for "+systemStateChangeMessage.getUserId());
//        helper.setFrom("martinbikathi@gmail.com");
//        javaMailSender.send(mimeMessage);
//        log.info("Add post notification successfully sent...");
//    }
//
//    public void viewPostNotification(String userEmail) throws MessagingException{
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//        SystemStateChangeMessage systemStateChangeMessage=restTemplate
//                .getForObject(BASE_URL+"/notification/view-post", SystemStateChangeMessage.class);
//        log.info("Talking to notification service for message body for notification view-post endpoint.");
//        helper.setText(systemStateChangeMessage.getMessage(),true);
//        helper.setTo(systemStateChangeMessage.getUserId());
//        helper.setSubject("view-post alert for "+systemStateChangeMessage.getUserId());
//        helper.setFrom("martinbikathi@gmail.com");
//        javaMailSender.send(mimeMessage);
//        log.info("view post notification successfully sent...");
//    }
//
//    public void deletePostTypeNotification(String userEmail) throws MessagingException{
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//        SystemStateChangeMessage systemStateChangeMessage=restTemplate
//                .getForObject(BASE_URL+"/notification/delete-post-type", SystemStateChangeMessage.class);
//        log.info("Talking to notification service for message body for notification delete post type endpoint.");
//        helper.setText(systemStateChangeMessage.getMessage(),true);
//        helper.setTo(systemStateChangeMessage.getUserId());
//        helper.setSubject("delete-post-type alert for "+systemStateChangeMessage.getUserId());
//        helper.setFrom("martinbikathi@gmail.com");
//        javaMailSender.send(mimeMessage);
//        log.info("delete-post-type notification successfully sent...");
//    }
//
//    public void addPostTypeNotification(String userEmail) throws MessagingException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//        SystemStateChangeMessage systemStateChangeMessage=restTemplate
//                .getForObject(BASE_URL+"/notification/add-post-type", SystemStateChangeMessage.class);
//        helper.setText(systemStateChangeMessage.getMessage());
//        helper.setTo(systemStateChangeMessage.getUserId());
//        helper.setSubject("add post type for "+systemStateChangeMessage.getUserId());
//        helper.setFrom("martinbikathi@gmail.com");
//        javaMailSender.send(mimeMessage);
//        log.info("add post type notification successfully sent...");
//    }
//    public void getPostTypeNotification(String userEmail) throws MessagingException{
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//        SystemStateChangeMessage systemStateChangeMessage=restTemplate
//                .getForObject(BASE_URL+"/notification/get-post-type", SystemStateChangeMessage.class);
//        helper.setText(systemStateChangeMessage.getMessage());
//        helper.setTo(systemStateChangeMessage.getUserId());
//        helper.setSubject("get post type for "+systemStateChangeMessage.getUserId());
//        helper.setFrom("martinbikathi@gmail.com");
//        javaMailSender.send(mimeMessage);
//        log.info("get post type notification successfully sent...");
//    }
//
//    public void deletePostTypeNotification(String userEmail, String typeId) throws MessagingException{
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//        SystemStateChangeMessage systemStateChangeMessage=restTemplate
//                .getForObject(BASE_URL+"/notification/get-post-type", SystemStateChangeMessage.class);
//        helper.setText(systemStateChangeMessage.getMessage());
//        helper.setTo(systemStateChangeMessage.getUserId());
//        helper.setSubject("delete post type for "+systemStateChangeMessage.getUserId());
//        helper.setFrom("martinbikathi@gmail.com");
//        javaMailSender.send(mimeMessage);
//        log.info("delete post type notification successfully sent...");
//
//    }
}
