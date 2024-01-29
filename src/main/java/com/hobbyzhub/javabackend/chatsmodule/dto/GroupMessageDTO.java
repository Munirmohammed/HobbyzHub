package com.hobbyzhub.javabackend.chatsmodule.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMessageDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fromUserId;
	private String toGroupId;
	private String message;
	private String dateSent;
}
