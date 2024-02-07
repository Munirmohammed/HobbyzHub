package com.hobbyzhub.javabackend.chatsmodule.repository;

import com.hobbyzhub.javabackend.chatsmodule.entity.GroupChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatRepository extends MongoRepository<GroupChat, String> {

}
