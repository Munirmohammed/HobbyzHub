package com.hobbyzhub.javabackend.chatsmodule;

import com.hobbyzhub.javabackend.chatsmodule.payload.request.CreatePrivateChatRequest;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.GroupMessageDTO;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.PrivateMessageDTO;
import com.hobbyzhub.javabackend.chatsmodule.service.StompMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StompMessageController {
	@Autowired
	StompMessageService stompMessageService;
	
	@MessageMapping("/private")
	public void privateMessage(@Payload PrivateMessageDTO messagePayload) {
		Boolean result = stompMessageService.sendPrivateMessage(messagePayload.getToUserId(), messagePayload);
		if(result == Boolean.TRUE) {
			log.info(
				"Successfully sent private message fromUserId: {} toUserId: {}", 
				messagePayload.getFromUserId(),
				messagePayload.getToUserId()
			);
		} else {
			log.error(
				"Failed to send private message fromUserId: {} toUserId: {}",
				messagePayload.getFromUserId(),
				messagePayload.getToUserId()
			);
		}
	}
	
	@MessageMapping("/group")
	public void groupMessage(@Payload GroupMessageDTO messagePayload) {
		boolean result = stompMessageService.sendGroupMessage(messagePayload.getToGroupId(), messagePayload);
		if(result == Boolean.TRUE) {
			log.info(
				"Successfully sent group message fromUserId: {} toGroupId: {}", 
				messagePayload.getFromUserId(),
				messagePayload.getToGroupId()
			);
		} else {
			log.error(
				"Failed to send group message fromUserId: {} toGroupId: {}",
				messagePayload.getFromUserId(),
				messagePayload.getToGroupId()
			);
		}
	}
}
