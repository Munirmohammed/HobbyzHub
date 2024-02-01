package com.hobbyzhub.javabackend.postmodule.responses;/*
*
@author ameda
@project backend-modulith
@package com.hobbyzhub.javabackend.postmodule.responses
*
*/

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoryViewResponse {
    private List<String> storyImages = new ArrayList<>();
    private String storyCaption;
    private String username;
    private String creationTime;
}
