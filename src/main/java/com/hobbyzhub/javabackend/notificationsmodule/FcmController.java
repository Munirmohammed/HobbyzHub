package com.hobbyzhub.javabackend.notificationsmodule;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.hobbyzhub.javabackend.notificationsmodule.dto.MessageDTO;
import com.hobbyzhub.javabackend.notificationsmodule.service.FcmService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/notification")
public class FcmController {

    @Autowired
    private FcmService fcmService;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @PostMapping(value = "/single-notification")
    public ResponseEntity<GenericServiceResponse<String>> sendToSpecificDevice(
            @RequestBody MessageDTO messageDTO) throws FirebaseMessagingException {
        String response = fcmService.sendNotificationToSpecificDevice(messageDTO, messageDTO.getFirebaseToken());

        GenericServiceResponse<String> genericResponse = new GenericServiceResponse<>(
                apiVersion,
                organizationName,
                "Notification sent successfully",
                true,
                HttpStatus.OK.value(),
                response
        );

        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

    @Data
    public static class GenericServiceResponse<T> {
        private String apiVersion;
        private String organizationName;
        private String message;
        private boolean success;
        private int status;
        private T data;

        public GenericServiceResponse(String apiVersion, String organizationName, String message,
                                      boolean success, int status, T data) {
            this.apiVersion = apiVersion;
            this.organizationName = organizationName;
            this.message = message;
            this.success = success;
            this.status = status;
            this.data = data;
        }
    }
}
