package com.hobbyzhub.javabackend.notificationsmodule.entity;

import com.hobbyzhub.javabackend.notificationsmodule.dto.MessageDTO;

public class Notification {
    private String userId;
    private MessageDTO message;

    public Notification(String userId, MessageDTO message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public MessageDTO getMessage() {
        return message;
    }
}

