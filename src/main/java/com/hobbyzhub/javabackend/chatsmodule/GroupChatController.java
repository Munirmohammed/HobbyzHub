package com.hobbyzhub.javabackend.chatsmodule;

import com.hobbyzhub.javabackend.chatsmodule.entity.GroupChat;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.CreateGroupChatRequest;
import com.hobbyzhub.javabackend.chatsmodule.payload.request.GroupChatOpRequest;
import com.hobbyzhub.javabackend.chatsmodule.payload.response.GroupChatResponse;
import com.hobbyzhub.javabackend.chatsmodule.service.GroupChatService;
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

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chats/groups")
public class GroupChatController {
    @Autowired
    private GroupChatService groupChatService;

    @Autowired
    private ChatModelUtils chatModelUtils;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewGroupChat(@RequestBody CreateGroupChatRequest request) {
        GroupChat newGroupChat = new GroupChat();
        newGroupChat.setChatId(chatModelUtils.generateChatId());
        newGroupChat.setChatType("group");
        newGroupChat.setGroupName(request.getGroupName());
        newGroupChat.setGroupDescription(request.getGroupDescription());
        newGroupChat.setGroupIcon(request.getGroupIcon());
        newGroupChat.setAdmins(new ArrayList<>(request.getChatAdmins()));
        newGroupChat.setChatParticipants(new ArrayList<>(request.getChatParticipants()));

        // save the new group chat
        newGroupChat = groupChatService.createGroupChat(newGroupChat);

        // build the response
        GroupChatResponse response = new GroupChatResponse(
            newGroupChat.getChatId(),
            newGroupChat.getChatType(),
            newGroupChat.getGroupName(),
            newGroupChat.getGroupDescription(),
            newGroupChat.getGroupIcon(),
            newGroupChat.getDateTimeCreated(),
            new ArrayList<>(),
            new ArrayList<>()
        );

        // configure the lists
        response.setChatParticipants(newGroupChat.getChatParticipants().parallelStream().map(chatModelUtils::deriveUserInformation).toList());
        response.setChatAdmins(newGroupChat.getAdmins().parallelStream().map(chatModelUtils::deriveUserInformation).toList());

        log.info("Successfully created new group chat");
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully created new group chat",
            true,
            HttpStatus.OK.value(),
            response)
        );
    }

    @PostMapping(value = "/get-group", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGroupChat(@RequestBody GroupChatOpRequest request) {
        GroupChat groupChat = groupChatService.getGroupChatById(request.getGroupChatId());
        if(Objects.isNull(groupChat)) {
            return ResponseEntity.ok().body(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Chat does not exist",
                true,
                HttpStatus.OK.value(),
                null)
            );
        }

        GroupChatResponse response = new GroupChatResponse(
                groupChat.getChatId(),
                groupChat.getChatType(),
                groupChat.getGroupName(),
                groupChat.getGroupDescription(),
                groupChat.getGroupIcon(),
                groupChat.getDateTimeCreated(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        // configure the lists
        response.setChatParticipants(groupChat.getChatParticipants().parallelStream().map(chatModelUtils::deriveUserInformation).toList());
        response.setChatAdmins(groupChat.getAdmins().parallelStream().map(chatModelUtils::deriveUserInformation).toList());

        log.info("Successfully retrieved group chat");
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully created new group chat",
            true,
            HttpStatus.OK.value(),
            response)
        );
    }

    @PostMapping(value = "/get-for-user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGroupsForUser(@RequestBody GroupChatOpRequest request) {
        List<GroupChat> userGroupChat = groupChatService.getGroupsForUser(request.getMemberId(), request.getPage(), request.getSize());
        List<GroupChatResponse> responseList = userGroupChat.parallelStream().map(groupChat -> {
            GroupChatResponse response = new GroupChatResponse(
                groupChat.getChatId(),
                groupChat.getChatType(),
                groupChat.getGroupName(),
                groupChat.getGroupDescription(),
                groupChat.getGroupIcon(),
                groupChat.getDateTimeCreated(),
                new ArrayList<>(),
                new ArrayList<>()
            );

            response.setChatAdmins(groupChat.getAdmins().parallelStream().map(chatModelUtils::deriveUserInformation).toList());
            response.setChatParticipants(groupChat.getChatParticipants().parallelStream().map(chatModelUtils::deriveUserInformation).toList());
            return response;
        }).toList();

        log.info("Successfully retrieved list for user with id: {}", request.getMemberId());
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully got paged list of user groups",
            true,
            HttpStatus.OK.value(),
            responseList)
        );
    }

    @PutMapping(value = "/add-member", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addMember(@RequestBody GroupChatOpRequest request) {
        groupChatService.addNewMember(request.getMemberId(), request.getGroupChatId());

        log.info("Successfully added member to group chat");
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully added new member to group chat",
            true,
            HttpStatus.OK.value(),
            null)
        );
    }

    @PatchMapping(value = "/remove-member", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeMember(@RequestBody GroupChatOpRequest request) {
        groupChatService.removeMember(request.getMemberId(), request.getGroupChatId());

        log.info("Successfully removed member group chat");
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully removed member from group chat",
            true,
            HttpStatus.OK.value(),
            null)
        );
    }

    @PatchMapping(value = "/make-admin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> makeMemberAdmin(@RequestBody GroupChatOpRequest request) {
        groupChatService.makeMemberAdmin(request.getMemberId(), request.getGroupChatId());

        log.info("Successfully made member admin");
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully made member admin",
            true,
            HttpStatus.OK.value(),
            null)
        );
    }

    @DeleteMapping(value = "/delete-group", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGroup(@RequestBody GroupChatOpRequest request) {
        groupChatService.deleteGroupChat(request.getGroupChatId());

        log.info("Successfully made member admin");
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully deleted group by id: " + request.getGroupChatId(),
            true,
            HttpStatus.OK.value(),
            null)
        );
    }
}
