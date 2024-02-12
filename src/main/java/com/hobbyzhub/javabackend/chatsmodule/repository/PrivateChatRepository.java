package com.hobbyzhub.javabackend.chatsmodule.repository;

import com.hobbyzhub.javabackend.chatsmodule.entity.PrivateChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateChatRepository extends MongoRepository<PrivateChat, String> {
    Page<PrivateChat> findByChatParticipantsContains(String participantId, Pageable pageable);
    PrivateChat findByParticipantAOrParticipantB(String participantId);
}
