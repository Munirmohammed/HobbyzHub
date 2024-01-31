package com.hobbyzhub.javabackend.postmodule.requests;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreatePost {
    private String caption;
    private List<HashTagRequest> hashTags =new ArrayList<>();
}
