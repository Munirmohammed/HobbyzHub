package com.hobbyzhub.javabackend.chatsmodule.payload.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePrivateChatRequest {
    private List<String> chatParticipants;
    private String dateTimeCreated;
}
