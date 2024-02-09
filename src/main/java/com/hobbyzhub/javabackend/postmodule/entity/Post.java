package com.hobbyzhub.javabackend.postmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class Post implements Serializable {
    @Id
    private String postId;
    private String userId;
    private String caption;

    @ElementCollection(targetClass = String.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "imageUrls",joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "imageUrls",nullable = false,length=100000,columnDefinition = "LONGBLOB")
    @Lob
    private LocalDateTime postTime;
    @JsonManagedReference
    @OneToMany(mappedBy = "postLike",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Like> likes = new HashSet<>();
    @JsonManagedReference
    @OneToMany(mappedBy = "postComment",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Comment> comments =new HashSet<>();
    private boolean status;    private List<String> imageUrls =new ArrayList<>();

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "post_hashtags",
    joinColumns = @JoinColumn(name = "postId"),
    inverseJoinColumns = @JoinColumn(name = "hashtagId"))
    private List<HashTag> hashTags =new ArrayList<>();

    private String username;
    @Lob
    @Column(name = "profile_image", columnDefinition = "BLOB")
    private String profileImage;
    private Integer  likeCount =0; // always increment and decrement when necessary...
    private Boolean deActivateComments = false; // should be false by default...
}
