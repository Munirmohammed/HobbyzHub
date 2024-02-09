package com.hobbyzhub.javabackend.chatsmodule.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericChat {
    private String chatType;
    private List<String> chatParticipants; // in a group chat, this list will be limited in size
    private String dateTimeCreated;
}
