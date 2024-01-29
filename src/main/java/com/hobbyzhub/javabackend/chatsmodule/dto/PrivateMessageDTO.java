package com.hobbyzhub.javabackend.chatsmodule.dto;

import java.io.Serializable;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessageDTO {
	private String fromUserId;
	private String toUserId;
	private String message;
	private String dateTimeSent;
	private String chatId;
	private String type;
}
