package com.hobbyzhub.javabackend.chatsmodule.service;

import java.time.LocalDate;

import com.hobbyzhub.javabackend.chatsmodule.entity.MessageModel;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.MessageDTO;
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
	
	public boolean createGroupDestination(String groupId) {
		try {
			String dateToday = LocalDate.now().toString();
			MessageDTO internalGroupMessage = MessageDTO.builder()
					.messageString("Group created on " + dateToday)
					.metadata(new MessageModel.MessageMetadata(
						"",
						"group-" + groupId,
						"Hobbyzhub"))
			.build();
			
			String internalMessage = new ObjectMapper().writer().withDefaultPrettyPrinter()
				.writeValueAsString(internalGroupMessage);
			
			jmsTemplate.send("group-" + groupId, messageCreator -> {
	            TextMessage deliverable = messageCreator.createTextMessage();
	            deliverable.setText(internalMessage);
	            return deliverable;
	        });
			
			convenienceMethods.storeMessage(internalGroupMessage);
			log.info("JMSTemplate created new group destination of ID: {}", "group-" + groupId);
			return true;
		} catch(Exception ex) {
			log.error("JMSTemplate error creating group destination: {}", ex.getMessage());
			return false;
		}
	}
}
