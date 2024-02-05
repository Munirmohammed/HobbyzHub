package com.hobbyzhub.javabackend.chatsmodule;

import com.hobbyzhub.javabackend.chatsmodule.payload.request.MessageDTO;
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
	public void privateMessage(@Payload MessageDTO messagePayload) {
		Boolean result = stompMessageService.sendPrivateMessage(messagePayload.getMetadata().getToDestinationId(), messagePayload);
		if(result == Boolean.TRUE) {
			log.info(
				"Successfully sent private message fromUserId: {} toUserId: {}", 
				messagePayload.getMetadata().getFromUserId(),
				messagePayload.getMetadata().getToDestinationId()
			);
		} else {
			log.error(
				"Failed to send private message fromUserId: {} toUserId: {}",
				messagePayload.getMetadata().getFromUserId(),
				messagePayload.getMetadata().getToDestinationId()
			);
		}
	}
	
	@MessageMapping("/group")
	public void groupMessage(@Payload MessageDTO messagePayload) {
		boolean result = stompMessageService.sendGroupMessage(messagePayload.getMetadata().getToDestinationId(), messagePayload);
		if(result == Boolean.TRUE) {
			log.info(
				"Successfully sent group message fromUserId: {} toGroupId: {}",
				messagePayload.getMetadata().getFromUserId(),
				messagePayload.getMetadata().getToDestinationId()
			);
		} else {
			log.error(
				"Failed to send group message fromUserId: {} toGroupId: {}",
				messagePayload.getMetadata().getFromUserId(),
				messagePayload.getMetadata().getToDestinationId()
			);
		}
	}
}
