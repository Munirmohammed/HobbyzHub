package com.hobbyzhub.javabackend.followersmodule.service;

import com.hobbyzhub.javabackend.followersmodule.entity.UserFollowings;
import com.hobbyzhub.javabackend.followersmodule.repository.MappingsRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author: Munir Mohammed
 */
@Service
public class UserFollowersService {

    @Autowired
    private MappingsRepository mappingsRepository;

    private final Logger logger = LoggerFactory.getLogger(UserFollowersService.class);

    public List<UserFollowings> getFollowersList(String toUserId, Integer page, Integer size) {
        try {
            // Set default values if not provided
            page = (page != null) ? page : 0;
            size = (size != null) ? size : 100;

            Pageable paging = PageRequest.of(page, size);
            Page<UserFollowings> followersPage = mappingsRepository.findAllByToUserId(toUserId, paging);
            return new ArrayList<>(followersPage.getContent());
        } catch (Exception ex) {
            logger.error("Error fetching paginated followers: {}", ex.getMessage(), ex);
            throw new ServiceException("Error fetching paginated followers.", ex);
        }
    }

    public boolean isFollowing(String fromUserId, String toUserId) {
        Optional<UserFollowings> existingRelationship = mappingsRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
        return existingRelationship.isPresent();
    }

    @Transactional
    public void deleteAllFollowers(String userId) {
        try {
            mappingsRepository.deleteAllByToUserId(userId);
        } catch (Exception ex) {
            logger.error("Error deleting all followers: {}", ex.getMessage(), ex);
            throw new ServiceException("Error deleting all followers.", ex);
        }
    }
}

