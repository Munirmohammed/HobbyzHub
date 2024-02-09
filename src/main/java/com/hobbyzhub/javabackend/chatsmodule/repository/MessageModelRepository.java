package com.hobbyzhub.javabackend.chatsmodule.repository;

import com.hobbyzhub.javabackend.chatsmodule.entity.MessageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageModelRepository extends MongoRepository<MessageModel, String> {
    /**
     * A custom method to help find the list of messages for a particular chatId
     */
    Page<MessageModel> findAllByChatId(String chatId, Pageable page);
    void deleteAllByChatId(String chatId);
}
