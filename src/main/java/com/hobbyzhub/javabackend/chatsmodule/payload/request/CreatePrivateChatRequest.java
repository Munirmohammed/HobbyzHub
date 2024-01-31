package com.hobbyzhub.javabackend.chatsmodule.payload.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePrivateChatRequest {
    private String myUserId;
    private String otherUserId;
    private String dateTimeCreated;
}
