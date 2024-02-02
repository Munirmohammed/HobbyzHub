package com.hobbyzhub.javabackend.postmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public final class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String commentId;
    private Integer commentCount=0;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="post_id", nullable = true)
    private Post postComment;
    @Column(columnDefinition = "TEXT")
    private String message;
    @JsonManagedReference
    @OneToOne(mappedBy = "comment")
    private Like like;
    private String username;
    @Lob
    @Column(name = "profile_image", columnDefinition = "BLOB")
    private String profileImage;
    private LocalDateTime commentTime;
    private Boolean deActivateComments = false;
}
