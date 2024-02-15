package com.hobbyzhub.javabackend.notificationsmodule.service;

import com.google.firebase.messaging.*;
import com.hobbyzhub.javabackend.notificationsmodule.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FcmService {

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    public String sendNotificationToSpecificDevice(MessageDTO messageDTO, String token) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(messageDTO.getSubject())
                .setBody(messageDTO.getContent())
                .setImage(messageDTO.getImage())
                .build();
        Message message = Message.builder()
                .setToken(token) // Updated to use token from MessageDTO
                .setNotification(notification)
                .putAllData(messageDTO.getData())
                .build();
        return firebaseMessaging.send(message);
    }

    public BatchResponse sendNotificationToMultipleDevices(MessageDTO messageDTO, List<String> tokens) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(messageDTO.getSubject())
                .setBody(messageDTO.getContent())
                .setImage(messageDTO.getImage())
                .build();
        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(notification)
                .putAllData(messageDTO.getData())
                .build();
        return firebaseMessaging.sendMulticast(message);
    }

    public void subscribeToTopic(List<String> tokens, String topic) throws FirebaseMessagingException {
        firebaseMessaging.subscribeToTopic(tokens, topic);
    }

    public void unsubscribeFromTopic(List<String> tokens, String topic) throws FirebaseMessagingException {
        firebaseMessaging.unsubscribeFromTopic(tokens, topic);
    }

    public String sendNotificationToTopic(MessageDTO messageDTO, String topic) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(messageDTO.getSubject())
                .setBody(messageDTO.getContent())
                .setImage(messageDTO.getImage())
                .build();
        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(notification)
                .putAllData(messageDTO.getData())
                .build();
        return firebaseMessaging.send(message);
    }
}
