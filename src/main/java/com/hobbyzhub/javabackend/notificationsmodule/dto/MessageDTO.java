package com.hobbyzhub.javabackend.notificationsmodule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String subject;
    private String content;
    private Map<String,String> data;
    private  String image;
    private String firebaseToken; // New field to hold the Firebase token
}