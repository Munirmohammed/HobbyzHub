package com.hobbyzhub.javabackend.postmodule.requests;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTagRequest {
    private String tagName;
}
