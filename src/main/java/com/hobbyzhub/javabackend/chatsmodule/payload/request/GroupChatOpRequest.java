package com.hobbyzhub.javabackend.chatsmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatOpRequest {
    private String memberId, groupChatId;
}
