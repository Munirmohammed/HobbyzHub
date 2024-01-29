package com.hobbyzhub.javabackend.chatsmodule.service;

import com.hobbyzhub.javabackend.chatsmodule.entity.MessageModel;
import com.hobbyzhub.javabackend.chatsmodule.repository.MessageModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageModelService {
    @Autowired
    private MessageModelRepository messageModelRepository;

    public Page<MessageModel> getPagedMessageList(Integer pageSize, Integer pageNumber, String chatId) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return messageModelRepository.findAllByChatId(chatId, page);
    }
}
