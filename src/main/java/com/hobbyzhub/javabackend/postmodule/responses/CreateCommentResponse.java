package com.hobbyzhub.javabackend.postmodule.responses;


import com.hobbyzhub.javabackend.postmodule.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentResponse {
    private String commentId;
    private Integer commentCount;
    private String message;
    private Like like;
    private String username;
    private String profileImage;
    private LocalDateTime commentTime;
}
