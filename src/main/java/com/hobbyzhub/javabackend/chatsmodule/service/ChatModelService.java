package com.hobbyzhub.javabackend.chatsmodule.service;

import com.hobbyzhub.javabackend.chatsmodule.entity.ChatModel;
import com.hobbyzhub.javabackend.chatsmodule.repository.ChatModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatModelService {
    @Autowired
    ChatModelRepository chatModelRepository;

    @Autowired
    MongoTemplate chatModelTemplate;

    public void createNewChat(ChatModel newChatModel) {
        chatModelRepository.save(newChatModel);
    }

    public void getChatsByParticipantId() {}

    public void deleteChatById(String chatId, String participantId) {
        ChatModel existingModel = chatModelRepository.findById(chatId).get();
        existingModel.getChatParticipants().remove(participantId);

        // if no one else is left in the chat participants, delete all the messages
        // else the other participant has not deleted this chat
        if(existingModel.getChatParticipants().isEmpty()) {
            // TODO: figure out how to delete all the chat messages of a chat
            chatModelRepository.deleteById(existingModel.getChatId());
        } else {
            // else save the model with one less participant since the other person has not deleted yet
            chatModelRepository.save(existingModel);
        }
    }

    public boolean chatModelExistsById(String id) {
        return chatModelRepository.existsById(id);
    }
}