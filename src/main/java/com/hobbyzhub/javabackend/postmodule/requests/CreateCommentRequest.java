package com.hobbyzhub.javabackend.postmodule.requests;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentRequest {
    private String message;
}
