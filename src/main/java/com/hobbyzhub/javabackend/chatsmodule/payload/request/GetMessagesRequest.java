package com.hobbyzhub.javabackend.chatsmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMessagesRequest {
    private String chatId;
    private Integer page;
    private Integer size;
}
