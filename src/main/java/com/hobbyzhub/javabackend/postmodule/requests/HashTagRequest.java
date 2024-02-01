package com.hobbyzhub.javabackend.postmodule.requests;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashTagRequest {
    private String tagName;
}
