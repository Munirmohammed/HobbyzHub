package com.hobbyzhub.javabackend.postmodule.responses;

import com.hobbyzhub.javabackend.postmodule.entity.Comment;
import com.hobbyzhub.javabackend.postmodule.entity.HashTag;
import com.hobbyzhub.javabackend.postmodule.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostResponseBody {
    private String postId;
    private String caption;
    private LocalDateTime postTime;
    private Boolean status;
    private String userId;
    private String username;
    private String profileImage;
    private List<HashTag> hashTags = new ArrayList<>();
    private Set<Comment> comments = new HashSet<>();
    private Set<Like> likes = new HashSet<>();
    private List<String> imageUrls = new ArrayList<>();
}

