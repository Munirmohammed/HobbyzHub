package com.hobbyzhub.javabackend.chatsmodule;

import com.hobbyzhub.javabackend.chatsmodule.entity.MessageModel;
import com.hobbyzhub.javabackend.chatsmodule.service.MessageModelService;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class MessageModelController {
    @Autowired
    MessageModelService messageModelService;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    public ResponseEntity<?> getPagedMessageList(
        @PathVariable String chatId,
        @RequestParam(defaultValue = "0") String page,
        @RequestParam(defaultValue = "150", required = false) String size) {

        // convert all parameters down to proper integers
        Integer pageNumber = Integer.valueOf(page);
        Integer pageSize = Integer.valueOf(size);
        try {
            List<MessageModel> messageList = messageModelService.getPagedMessageList(pageSize, pageNumber, chatId).stream().toList();
            if(messageList.isEmpty()) {
                log.warn("Getting empty chat message list for chatId: {}", chatId);
                return new ResponseEntity<>(new GenericResponse<>(
                    apiVersion,
                    organizationName,
                    "Empty chat message list for that chat",
                    true,
                    HttpStatus.NO_CONTENT.value(), messageList),
                HttpStatus.OK);
            }

            log.info("Gotten chat message list successfully for chatId: {}", chatId);
            return new ResponseEntity<>(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully gotten chat message list successfully",
                true,
                HttpStatus.OK.value(),
                messageList),
            HttpStatus.OK);
        } catch(Exception ex) {
            log.error("Error getting paged list of messages for chatId: {}. Caused by: {}", chatId, ex.getMessage());
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
