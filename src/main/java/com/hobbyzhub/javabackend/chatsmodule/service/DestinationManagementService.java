package com.hobbyzhub.javabackend.chatsmodule.service;

import java.time.LocalDate;

import com.hobbyzhub.javabackend.chatsmodule.payload.request.CreatePrivateChatRequest;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.GroupMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DestinationManagementService {
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	MessageStoreConvenienceMethods convenienceMethods;
	
	/*
	public boolean createPrivateDestination(String userId) {
		PrivateMessageDTO internalPrivateMessage = 
			PrivateMessageDTO.builder()
			.fromUserId("Hobbyzhub")
			.toUserId(userId)
			.message("Welcome to Hobbyzhub. We're thrilled to have you.")
			.dateSent(Mess)
			.build();
		
		try {
			String internalMessage = new ObjectMapper().writer().withDefaultPrettyPrinter()
				.writeValueAsString(internalPrivateMessage);
			
			jmsTemplate.send("user-" + userId, messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText(internalMessage);
                return deliverable;
            });
			
			convenienceMethods.storePrivateMessage(internalPrivateMessage);
			log.info("JMSTemplate created new private destination of ID: {}", "user-" + userId);
			return true;
		} catch(Exception ex) {
			log.error("JMSTemplate error creating private destination: {}", ex.getMessage());
			return false;
		}
	}
	 */
	
	public boolean createGroupDestination(String groupId) {
		try {
			String dateToday = LocalDate.now().toString();
			GroupMessageDTO internalGroupMessage =
				GroupMessageDTO.builder()
				.fromUserId("Hobbyzub")
				.toGroupId(groupId)
				.message("Group Created On: " + dateToday)
				.dateSent(dateToday)
				.build();
			
			String internalMessage = new ObjectMapper().writer().withDefaultPrettyPrinter()
				.writeValueAsString(internalGroupMessage);
			
			jmsTemplate.send("group-" + groupId, messageCreator -> {
	            TextMessage deliverable = messageCreator.createTextMessage();
	            deliverable.setText(internalMessage);
	            return deliverable;
	        });
			
			convenienceMethods.storeGroupChatMessage(internalGroupMessage);
			log.info("JMSTemplate created new group destination of ID: {}", "group-" + groupId);
			return true;
		} catch(Exception ex) {
			log.error("JMSTemplate error creating group destination: {}", ex.getMessage());
			return false;
		}
	}
}
