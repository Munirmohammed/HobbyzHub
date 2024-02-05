package com.hobbyzhub.javabackend.chatsmodule.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages_collection")
public class MessageModel implements Serializable {
    @Id
    private String messageModelId;
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
