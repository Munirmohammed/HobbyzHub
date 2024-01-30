package com.hobbyzhub.javabackend.chatsmodule.payload.response;

import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatModelResponse {
    private String chatId;
    private List<SharedAccountsInformation> chatParticipants;
}
