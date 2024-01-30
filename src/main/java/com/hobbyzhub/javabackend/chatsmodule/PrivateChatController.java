package com.hobbyzhub.javabackend.chatsmodule;

import com.hobbyzhub.javabackend.chatsmodule.entity.ChatModel;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.CreatePrivateChatRequest;
import com.hobbyzhub.javabackend.chatsmodule.payload.response.ChatModelResponse;
import com.hobbyzhub.javabackend.chatsmodule.service.ChatModelService;
import com.hobbyzhub.javabackend.chatsmodule.util.ChatModelUtils;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chats/private")
public class PrivateChatController {
    @Autowired
    private ChatModelService chatModelService;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @PostMapping(value = "/create-new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewPrivateChat(@RequestBody CreatePrivateChatRequest request) {
        String chatId = ChatModelUtils.generateChatId();
        ChatModel newChat = ChatModel.builder()
            .chatId(chatId)
            .dateTimeCreated(request.getDateTimeCreated())
            .chatParticipants(request.getChatParticipants())
        .build();

        chatModelService.createNewChat(newChat);
        ChatModelResponse response = new ChatModelResponse(chatId, new ArrayList<>());
        response.setChatParticipants(newChat.getChatParticipants().parallelStream().map(ChatModelUtils::deriveUserInformation).toList());
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully created new chat",
            true,
            HttpStatus.OK.value(),
            response
        ));
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getChatByParticipantId(
        @RequestParam(name = "participantId") String participantId,
        @RequestParam(name = "page", defaultValue = "1") Integer page,
        @RequestParam(name = "size", defaultValue = "100") Integer size) {
        List<ChatModel> chats = chatModelService.getChatsByParticipantId(participantId, page, 100);
        List<ChatModelResponse> responseList = chats.parallelStream().map(chatModel -> {
            ChatModelResponse chatModelResponse = new ChatModelResponse(chatModel.getChatId(), new ArrayList<>());
            chatModelResponse.setChatParticipants(chatModel.getChatParticipants().parallelStream().map(ChatModelUtils::deriveUserInformation).toList());
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


}
