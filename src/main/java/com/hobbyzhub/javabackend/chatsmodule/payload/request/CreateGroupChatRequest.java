package com.hobbyzhub.javabackend.chatsmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupChatRequest {
    private String groupName, groupDescription, groupIcon, dateTimeCreated;
    private List<String> chatParticipants, chatAdmins;
}
