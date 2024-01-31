package com.hobbyzhub.javabackend.chatsmodule;

import com.hobbyzhub.javabackend.chatsmodule.entity.ChatModel;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.CreatePrivateChatRequest;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.GetChatsForUserRequest;
import com.hobbyzhub.javabackend.chatsmodule.payload.response.ChatModelResponse;
import com.hobbyzhub.javabackend.chatsmodule.service.ChatModelService;
import com.hobbyzhub.javabackend.chatsmodule.util.ChatModelUtils;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import com.hobbyzhub.javabackend.sharedutils.UserDetailsImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chats/private")
public class PrivateChatController {
    @Autowired
    private ChatModelService chatModelService;

    @Autowired
    private ChatModelUtils chatModelUtils;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @PostMapping(value = "/create-new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewPrivateChat(@RequestBody CreatePrivateChatRequest request) {
        String chatId = chatModelUtils.generateChatId();
        ChatModel newChat = ChatModel.builder()
            .chatId(chatId)
            .dateTimeCreated(request.getDateTimeCreated())
            .chatParticipants(new ArrayList<>(List.of(request.getMyUserId(), request.getOtherUserId())))
        .build();
        newChat = chatModelService.createNewChat(newChat);

        // swap the indexes of the participants if required
        this.reduceIndexes(newChat.getChatParticipants(), request.getMyUserId());

        ChatModelResponse response = new ChatModelResponse(
            chatId,
            newChat.getChatParticipants().size() <= 2 ? "private" : "group",
            newChat.getDateTimeCreated(),
            new ArrayList<>());
        response.setChatParticipants(newChat.getChatParticipants().parallelStream().map(chatModelUtils::deriveUserInformation).toList());
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully created new chat",
            true,
            HttpStatus.OK.value(),
            response
        ));
    }

    @PostMapping(value = "/get-chats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getChatByParticipantId(@RequestBody GetChatsForUserRequest request) {
        List<ChatModel> chats = chatModelService.getChatsByParticipantId(request.getParticipantId(), request.getPage(), request.getSize());
        List<ChatModelResponse> responseList = chats.parallelStream().map(chatModel -> {
            ChatModelResponse chatModelResponse = new ChatModelResponse(
                chatModel.getChatId(),
                chatModel.getChatParticipants().size() <= 2 ? "private" : "group",
                chatModel.getDateTimeCreated(),
                new ArrayList<>());
            this.reduceIndexes(chatModel.getChatParticipants(), request.getParticipantId());
            chatModelResponse.setChatParticipants(chatModel.getChatParticipants().parallelStream().map(chatModelUtils::deriveUserInformation).toList());

            return chatModelResponse;
        }).toList();

        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully retrieved chats",
            true,
            HttpStatus.OK.value(),
            responseList
        ));
    }

    @DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteChat(@RequestParam String chatId, @RequestParam String userId) {
        chatModelService.deleteChatById(chatId, userId);

        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully deleted chat",
            true,
            HttpStatus.OK.value(),
            null
        ));
    }

    private void reduceIndexes(List<String> participantsList, String userId) {
        participantsList.remove(userId);
    }
}
