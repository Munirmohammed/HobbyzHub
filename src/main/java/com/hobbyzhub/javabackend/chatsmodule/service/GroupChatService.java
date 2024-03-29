package com.hobbyzhub.javabackend.chatsmodule.service;

import com.hobbyzhub.javabackend.chatsmodule.entity.GroupChat;
import com.hobbyzhub.javabackend.chatsmodule.repository.GroupChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupChatService {
    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private MessageModelService messageModelService;

    @Autowired
    private DestinationManagementService destinationManagementService;

    public GroupChat createGroupChat(GroupChat groupChat) {
        GroupChat createdGroupChat = groupChatRepository.save(groupChat);
        destinationManagementService.createGroupDestination(createdGroupChat.getChatId(), groupChat.getDateTimeCreated());

        return createdGroupChat;
    }

    public GroupChat getGroupChatById(String groupChatId) {
        return groupChatRepository.findById(groupChatId).orElse(null);
    }

    public List<GroupChat> getGroupsForUser(String userId, Integer page, Integer size) {
        Pageable pageInfo = PageRequest.of(page, size);
        return groupChatRepository.findByChatParticipantsContains(userId, pageInfo).toList();
    }

    // TODO: handle this later
    public void deleteGroupChat(String groupChatId) {
        groupChatRepository.deleteById(groupChatId);
        messageModelService.deleteMessagesByChatId(groupChatId);
    }

    public void makeMemberAdmin(String memberId, String groupChatId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId).orElse(null);
        if(groupChat != null && !groupChat.getAdmins().contains(memberId)) {
            groupChat.getAdmins().add(memberId);
            groupChatRepository.save(groupChat);
        }
    }

    public void addNewMember(String memberId, String groupChatId) throws IndexOutOfBoundsException {
        GroupChat groupChat = groupChatRepository.findById(groupChatId).orElse(null);
        if(groupChat != null && !groupChat.getChatParticipants().contains(memberId)) {
            if(groupChat.getChatParticipants().size() < 512) {
                groupChat.getChatParticipants().add(memberId);
                groupChatRepository.save(groupChat);
            } else {
                throw new IndexOutOfBoundsException("Group already at full capacity");
            }
        }
    }

    public void removeMember(String memberId, String groupChatId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId).orElse(null);
        if(groupChat != null && !groupChat.getChatParticipants().contains(memberId)) {
            groupChat.getChatParticipants().remove(memberId);
            groupChatRepository.save(groupChat);
        }
    }
}
