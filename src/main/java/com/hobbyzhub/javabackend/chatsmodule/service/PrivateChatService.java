package com.hobbyzhub.javabackend.chatsmodule.service;

import com.hobbyzhub.javabackend.chatsmodule.entity.PrivateChat;
import com.hobbyzhub.javabackend.chatsmodule.repository.PrivateChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PrivateChatService {
    @Autowired
    PrivateChatRepository privateChatRepository;

    @Autowired
    private MessageModelService messageModelService;

    @Autowired
    MongoTemplate chatModelTemplate;

    public PrivateChat createNewChat(String myParticipantId, PrivateChat newPrivateChat) {
        PrivateChat existingPrivateChat = privateChatRepository.findByParticipantAOrParticipantB(myParticipantId);
        if(Objects.isNull(existingPrivateChat)) {
            return privateChatRepository.save(newPrivateChat);
        } else {
            return existingPrivateChat;
        }
    }

    public List<PrivateChat> getChatsByParticipantId(String participantId, Integer page, Integer size) {
        Pageable pageInfo = PageRequest.of(page, size);
        return privateChatRepository.findByChatParticipantsContains(participantId, pageInfo).toList();
    }

    public void deleteChatById(String chatId, String participantId) {
        PrivateChat existingModel = privateChatRepository.findById(chatId).get();
        existingModel.getChatParticipants().remove(participantId);

        // if no one else is left in the chat participants, delete all the messages
        // else the other participant has not deleted this chat
        if(existingModel.getChatParticipants().isEmpty()) {
            privateChatRepository.deleteById(existingModel.getChatId());

            // on a separate thread, delete messages for that chat
            messageModelService.deleteMessagesByChatId(chatId);
        } else {
            // else save the model with one less participant since the other person has not deleted yet
            privateChatRepository.save(existingModel);
        }
    }

    public boolean chatModelExistsById(String id) {
        return privateChatRepository.existsById(id);
    }
}
