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
public class StoryCreateResponse {
    private String username;
    private String userProfileImage;
    private List<String> storyImages = new ArrayList<>();
    private String createdTime;
    private String email;
}
