package com.hobbyzhub.javabackend.postmodule.requests;/*
*
@author ameda
@project backend-modulith
@package com.hobbyzhub.javabackend.postmodule.requests
*
*/


import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoryRequest {
    private String storyCaption;
    private String email;
    private int storyDuration;

}
