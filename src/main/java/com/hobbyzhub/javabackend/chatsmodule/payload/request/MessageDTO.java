package com.hobbyzhub.javabackend.chatsmodule.payload.request;

import com.hobbyzhub.javabackend.chatsmodule.entity.MessageModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String messageString;
    private String chatId;
    private List<MessageModel.OptionalMedia> media; // can be null for a message with no media
    private MessageModel.MessageMetadata metadata;
}
