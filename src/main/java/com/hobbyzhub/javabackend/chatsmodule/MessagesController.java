package com.hobbyzhub.javabackend.chatsmodule;

import com.hobbyzhub.javabackend.chatsmodule.entity.MessageModel;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.GetMessagesRequest;
import com.hobbyzhub.javabackend.chatsmodule.service.MessageModelService;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chats/messages")
public class MessagesController {
    @Autowired
    MessageModelService messageModelService;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @PostMapping(value = "/retrieve")
    public ResponseEntity<?> getPagedMessageList(@RequestBody GetMessagesRequest request) {
        try {
            List<MessageModel> messageList = messageModelService.getPagedMessageList(request.getSize(), request.getPage(), request.getChatId());
            if(messageList.isEmpty()) {
                log.warn("Getting empty chat message list for chatId: {}", request.getChatId());
                return new ResponseEntity<>(new GenericResponse<>(
                    apiVersion,
                    organizationName,
                    "Empty chat message list for that chat",
                    true,
                    HttpStatus.NO_CONTENT.value(), messageList),
                HttpStatus.OK);
            }

            log.info("Gotten chat message list successfully for chatId: {}", request.getChatId());
            return new ResponseEntity<>(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully gotten chat message list successfully",
                true,
                HttpStatus.OK.value(),
                messageList),
            HttpStatus.OK);
        } catch(Exception ex) {
            log.error("Error getting paged list of messages for chatId: {}. Caused by: {}", request.getChatId(), ex.getMessage());
            return new ResponseEntity<>(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Error getting list of chat messages",
                false,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
