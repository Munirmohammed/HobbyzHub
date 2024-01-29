package com.hobbyzhub.javabackend.chatsmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fromUserId;
    private String toGroupId;
    private String message;
    private String dateSent;
}