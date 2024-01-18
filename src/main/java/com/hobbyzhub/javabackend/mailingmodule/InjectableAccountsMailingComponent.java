package com.hobbyzhub.javabackend.mailingmodule;

import com.hobbyzhub.javabackend.mailingmodule.service.MailingService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Component
public class InjectableAccountsMailingComponent {
    @Autowired
    private MailingService mailingService;
    
    @PutMapping(value = "verify-email/{userEmail}/{otpNumber}")
    public ResponseEntity<?> sendEmailVerificationMail(
        @PathVariable String userEmail, @PathVariable String otpNumber) {
        try {
            mailingService.sendEmailVerificationMail(userEmail, otpNumber);
            return ResponseEntity.ok(null);
        } catch(MessagingException ex) {
            log.error("Error encountered while sending email: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @PutMapping(value = "pass-reset/{userEmail}/{otpNumber}")
    public ResponseEntity<?> sendPasswordResetVerificationMail(
        @PathVariable String userEmail, @PathVariable String otpNumber) {
        try {
            mailingService.sendPasswordResetMail(userEmail, otpNumber);
            return ResponseEntity.ok(null);
        } catch(MessagingException ex) {
            log.error("Error encountered while sending email: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
