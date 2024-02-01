package com.hobbyzhub.javabackend.followersmodule.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Author: Munir Mohammed
 */

@Entity
@Table(name = "user_relationship")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "my_user_id", nullable = false)
    private String myUserId;

    @Column(name = "other_user_id", nullable = false)
    private String otherUserId;

    @Column(name = "is_following", nullable = false)
    private boolean isFollowing;

}

