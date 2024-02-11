package com.hobbyzhub.javabackend.chatsmodule.payload.response;

import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChatResponse {
    private String chatId;
    private String type;
    private String dateTimeCreated;
    private SharedAccountsInformation chatParticipantB;
}
