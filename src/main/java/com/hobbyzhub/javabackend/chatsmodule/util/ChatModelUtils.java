package com.hobbyzhub.javabackend.chatsmodule.util;

import com.hobbyzhub.javabackend.chatsmodule.entity.ChatModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ChatModelUtils {
    public static String dateTimeToString(ZonedDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mmz");
        return dateTime.format(formatter);
    }

    public static ZonedDateTime parseDateTimeString(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mmz");
        return ZonedDateTime.parse(dateTime, formatter);
    }

    public static String generateChatId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    public static void sortChatsFromLatestFirst(List<ChatModel> chats) {
        chats.sort((chat1, chat2) -> chat2.getDateTimeCreated().compareTo(chat1.getDateTimeCreated()));
    }
}
