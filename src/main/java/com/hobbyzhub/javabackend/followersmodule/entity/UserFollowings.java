package com.hobbyzhub.javabackend.followersmodule.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Author: Munir Mohammed
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class UserFollowings implements Serializable {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "from_user_id")
    private String fromUserId;
    
    @Column(name = "to_user_id")
    private String toUserId;
}
