package com.hobbyzhub.javabackend.chatsmodule.entity;

import com.hobbyzhub.javabackend.chatsmodule.type.GenericChat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "private_chats")
public class PrivateChat extends GenericChat implements Serializable {
    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

    @Id
    private String chatId;

    private String participantA;
    private String participantB;
}
