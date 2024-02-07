package com.hobbyzhub.javabackend.chatsmodule.entity;

import com.hobbyzhub.javabackend.chatsmodule.type.GenericChat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "group_chats")
public class GroupChat extends GenericChat implements Serializable {
    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

    @Id
    private String chatId;

    private String groupName;
    private String groupDescription;
    private String groupIcon;
    private List<String> admins;
}
