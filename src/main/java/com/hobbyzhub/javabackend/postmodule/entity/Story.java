package com.hobbyzhub.javabackend.postmodule.entity;/*
*
@author ameda
@project backend-modulith
@package com.hobbyzhub.javabackend.postmodule.entity
*
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stories")
@Builder
public final class Story {
    @Id
    private String storyId;
    @ElementCollection(targetClass = String.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "storyImages",joinColumns = @JoinColumn(name = "story_id"))
    @Column(name = "storyImages",nullable = false,length=100000,columnDefinition = "LONGBLOB")
    @Lob
    private List<String> storyImages = new ArrayList<>();
    @NotNull
    private String creationTime;
    private String storyCaption;
    /*
    * deletion time should null for a start
    * */
    private String deletionTime;

    @NotNull
    private String username;
    @Lob
    @Column(name = "profile_image", columnDefinition = "BLOB")
    private String userProfileImage;

    @NotNull
    private String email;
    private Integer duration;
    @NotNull
    private Boolean expired;
    private int remainingTime;
}
