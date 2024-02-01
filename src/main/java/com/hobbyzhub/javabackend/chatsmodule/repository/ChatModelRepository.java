package com.hobbyzhub.javabackend.chatsmodule.repository;

import com.hobbyzhub.javabackend.chatsmodule.entity.ChatModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatModelRepository extends MongoRepository<ChatModel, String> {
    Page<ChatModel> findByChatParticipantsContains(String participantId, Pageable pageable);
    ChatModel findByChatParticipantsIsContainingAllIgnoreCase(List<String> participants);
}
