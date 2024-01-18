package com.hobbyzhub.javabackend.mailingmodule.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemStateChangeMessage {
    private String stateChangeId;
    private String userId;
    private String message;
    private LocalDateTime sendTime;
}
