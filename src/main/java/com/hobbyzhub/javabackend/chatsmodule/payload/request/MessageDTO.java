package com.hobbyzhub.javabackend.chatsmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String messageString;
    private List<OptionalMedia> media; // can be null for a message with no media
    private MessageMetadata metadata;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OptionalMedia {
        private String mediaUrl;
        private String type;
        private Long size; // (in MBs)e.g 10
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageMetadata {
        private String dateTimeSent; // DD-MM-YY, HH:MM
        private String toDestinationId; // the destination ID can be a queue or a topic
        private String fromUserId;
        private String chatId;
    }
}
