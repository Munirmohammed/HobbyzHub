package com.hobbyzhub.javabackend.chatsmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessageDTO {
    private String fromUserId;
    private String toUserId;
    private String message;
    private String dateTimeSent;
    private String chatId;
    private String type;
}
