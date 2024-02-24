package com.hobbyzhub.javabackend.postmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class Like implements Serializable {
    @Id
    private String likeId;
    private String username;
    private Integer likeCount=1;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="post_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post postLike;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;
    @Lob
    @Column(name = "profile_image", columnDefinition = "BLOB")
    private String profileImage;
    private LocalDateTime likeTime;
    private String userId;
}
