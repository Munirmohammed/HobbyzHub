package com.hobbyzhub.javabackend.notificationsmodule.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PushNotificationRequest {

    private String title;
    private String message;
    private String token;
    private String topic;

}
