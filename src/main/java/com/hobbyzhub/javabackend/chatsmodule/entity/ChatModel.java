package com.hobbyzhub.javabackend.chatsmodule.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chats_collection")
public class ChatModel implements Serializable {
    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

    @Id
    private String chatId;
    private String chatType;
    private List<String> chatParticipants; // in a group chat, this list will be limited in size
    private String dateTimeCreated;
}
