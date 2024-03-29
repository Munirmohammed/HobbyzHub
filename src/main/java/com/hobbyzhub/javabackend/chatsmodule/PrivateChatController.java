package com.hobbyzhub.javabackend.chatsmodule;

import com.hobbyzhub.javabackend.chatsmodule.entity.PrivateChat;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.CreatePrivateChatRequest;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.GetChatsForUserRequest;
import com.hobbyzhub.javabackend.chatsmodule.payload.response.PrivateChatResponse;
import com.hobbyzhub.javabackend.chatsmodule.service.PrivateChatService;
import com.hobbyzhub.javabackend.chatsmodule.util.ChatModelUtils;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
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
import java.util.Objects;

@Data
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chats/private")
public class PrivateChatController {
    @Autowired
    private PrivateChatService privateChatService;

    @Autowired
    private ChatModelUtils chatModelUtils;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @PostMapping(value = "/create-new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewPrivateChat(@RequestBody CreatePrivateChatRequest request) {
        String chatId = chatModelUtils.generateChatId();
        PrivateChat newPrivateChat = new PrivateChat();
        newPrivateChat.setChatId(chatId);
        newPrivateChat.setChatType("private");
        newPrivateChat.setParticipantA(request.getMyUserId());
        newPrivateChat.setParticipantB(request.getOtherUserId());
        newPrivateChat.setDateTimeCreated(request.getDateTimeCreated());
        newPrivateChat.setChatParticipants(new ArrayList<>(List.of(request.getMyUserId(), request.getOtherUserId())));
        
        newPrivateChat = privateChatService.createNewChat(newPrivateChat);

        PrivateChatResponse response = new PrivateChatResponse(
            chatId,
            newPrivateChat.getChatType(),
            newPrivateChat.getDateTimeCreated(),
            chatModelUtils.deriveUserInformation(request.getOtherUserId()));
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
        List<PrivateChat> chats = privateChatService.getChatsByParticipantId(request.getParticipantId(), request.getPage(), request.getSize());
        List<PrivateChatResponse> responseList = chats.parallelStream().map(privateChat -> {
            Integer personBIndex = this.deduceIndex(privateChat.getChatParticipants(), request.getParticipantId());
            return new PrivateChatResponse(
                privateChat.getChatId(),
                privateChat.getChatType(),
                privateChat.getDateTimeCreated(),
                chatModelUtils.deriveUserInformation(privateChat.getChatParticipants().get(personBIndex))
            );
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
        privateChatService.deleteChatById(chatId, userId);

        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully deleted chat",
            true,
            HttpStatus.OK.value(),
            null
        ));
    }

    private Integer deduceIndex(List<String> participantsList, String userId) {
        Integer myParticipantIndex = participantsList.indexOf(userId);
        return Objects.equals(myParticipantIndex, 0) ? 1 : 0;
    }
}
