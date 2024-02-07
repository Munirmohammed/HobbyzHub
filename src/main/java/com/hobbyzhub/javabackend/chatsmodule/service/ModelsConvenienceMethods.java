package com.hobbyzhub.javabackend.chatsmodule.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ModelsConvenienceMethods {
    @Autowired
    private PrivateChatService privateChatService;

    public String checkIfChatModelExistsById(String fromUserId, String toUserId) {
        String idOptionA = String.format("%s-%s", fromUserId, toUserId);
        String idOptionB = String.format("%s-%s", toUserId, fromUserId);

        // check if one of the ids does exist
        if(privateChatService.chatModelExistsById(idOptionA)) {
            return idOptionA;
        } else if(privateChatService.chatModelExistsById(idOptionB)) {
            return idOptionB;
        }

        // should never return null
        return null;
    }
}
