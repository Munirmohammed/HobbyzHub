package com.hobbyzhub.javabackend.chatsmodule.payload.response;

import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatResponse {
    private String chatId;
    private String type;
    private String groupName;
    private String groupDescription;
    private String groupIcon;
    private String dateTimeCreated;
    private List<SharedAccountsInformation> chatParticipants;
    private List<SharedAccountsInformation> chatAdmins;
}
