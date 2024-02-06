package com.hobbyzhub.javabackend.chatsmodule.service;

import com.hobbyzhub.javabackend.chatsmodule.entity.MessageModel;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.MessageDTO;
import com.hobbyzhub.javabackend.chatsmodule.repository.MessageModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MessageStoreConvenienceMethods {
    @Autowired
    private MessageModelRepository messageModelRepository;

    @Autowired
    private ModelsConvenienceMethods modelsConvenienceMethods;

    public void storeMessage(MessageDTO privateMessageDTO) {
        // first convert the DTO to a MongoDB-Compatible message
        // the id of the message is manually generated

        log.info("attempting to store chat message");
        MessageModel message = MessageModel.builder()
            .chatId(privateMessageDTO.getChatId())
            .messageString(privateMessageDTO.getMessageString())
            .media(Objects.isNull(privateMessageDTO.getMedia()) ? null : new ArrayList<>(privateMessageDTO.getMedia()))
            .metadata(privateMessageDTO.getMetadata())
        .build();

        // then store the message in the MessageStore
        messageModelRepository.save(message);
        log.info("Stored private from UserId {} to UserId {}",
            privateMessageDTO.getMetadata().getFromUserId(),
            privateMessageDTO.getMetadata().getToDestinationId()
        );
    }
}
