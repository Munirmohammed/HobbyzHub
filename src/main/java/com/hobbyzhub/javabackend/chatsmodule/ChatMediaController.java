package com.hobbyzhub.javabackend.chatsmodule;

import com.hobbyzhub.javabackend.chatsmodule.service.ChatMediaService;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chats/media")
public class ChatMediaController {
    @Autowired
    private ChatMediaService chatMediaService;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadChatMediaFile(@RequestParam(name = "media") MultipartFile chatMedia) {
        String mediaUrl = chatMediaService.generateFileUrl(chatMedia);

        log.info("Successfully uploaded chat media");
        return ResponseEntity.ok().body(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully created new group chat",
                true,
                HttpStatus.OK.value(),
                mediaUrl)
        );
    }

}
