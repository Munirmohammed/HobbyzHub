package com.hobbyzhub.javabackend.helprequestmodule.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HelpRequest {
    private String message;
    private String fullName;
}
