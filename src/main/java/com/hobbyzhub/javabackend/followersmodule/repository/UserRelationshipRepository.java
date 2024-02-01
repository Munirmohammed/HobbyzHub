package com.hobbyzhub.javabackend.followersmodule.repository;


import com.hobbyzhub.javabackend.followersmodule.entity.UserRelationship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 *
 * Author: Munir Mohammed
 */
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, String> {
    UserRelationship findByMyUserIdAndOtherUserId(String myUserId, String otherUserId);

    List<UserRelationship> findByMyUserIdOrOtherUserId(String userId, String userId1);

    Page<UserRelationship> findByMyUserId(String myUserId, Pageable pageable);

    Page<UserRelationship> findByOtherUserIdAndIsFollowing(String myUserId, boolean b, PageRequest createdAt);

    boolean existsByMyUserIdAndOtherUserId(String myUserId, String otherUserId);

    int countByOtherUserId(String otherUserId);

    int countByMyUserId(String myUserId);
}
