package com.hobbyzhub.javabackend.chatsmodule.util;

import com.hobbyzhub.javabackend.chatsmodule.entity.ChatModel;
import com.hobbyzhub.javabackend.securitymodule.SharedAccounts;
import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Component
public class ChatModelUtils {
    @Autowired
    private SharedAccounts sharedAccounts;

    /**
    public static String dateTimeToString(ZonedDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mmz");
        return dateTime.format(formatter);
    }

    public static ZonedDateTime parseDateTimeString(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mmz");
        return ZonedDateTime.parse(dateTime, formatter);
    }
     */

    public String generateChatId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    public void sortChatsFromLatestFirst(List<ChatModel> chats) {
        chats.sort((chat1, chat2) -> chat2.getDateTimeCreated().compareTo(chat1.getDateTimeCreated()));
    }

    public SharedAccountsInformation deriveUserInformation(String userId) {
        return sharedAccounts.retrieveSharedAccount(userId);
    }
}
