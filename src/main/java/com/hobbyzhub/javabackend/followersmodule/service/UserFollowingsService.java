package com.hobbyzhub.javabackend.followersmodule.service;

import com.hobbyzhub.javabackend.followersmodule.entity.UserFollowings;
import com.hobbyzhub.javabackend.followersmodule.repository.MappingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for managing user followings.
 */

@Service
public class UserFollowingsService {
    
    private static final Logger log = LoggerFactory.getLogger(UserFollowingsService.class);

    @Autowired
    private MappingsRepository mappingsRepository;

//    @Autowired
//    private NotificationServiceFeign notificationServiceFeign; // Inject the Feign client

    /**
     * Adds a new following map.
     * 
     * @param userFollowing The user followings entity.
     */
    public void addFollowingMap(UserFollowings userFollowing) {
        try {
            mappingsRepository.save(userFollowing);

            // Send follow notification using Feign client
//            notificationServiceFeign.sendFollowNotification(userFollowing);


            log.info("Following map added successfully.");
        } catch (Exception ex) {
            log.error("Error adding following map: {}", ex.getMessage(), ex);
            throw new ServiceException("Error adding following map.", ex);
        }
    }

    /**
     * Deletes a following map.
     * 
     * @param fromUserId The user who is following.
     * @param toUserId The user who is being followed.
     */
    @Transactional
    public void deleteFollowingMap(String fromUserId, String toUserId) {
        try {
            mappingsRepository.deleteFollowingMap(fromUserId, toUserId);
            log.info("Following map deleted successfully.");
        } catch (Exception ex) {
            log.error("Error deleting following map: {}", ex.getMessage(), ex);
            throw new ServiceException("Error deleting following map.", ex);
        }
    }

    /**
     * Deletes all following maps for a user.
     * 
     * @param fromUserId The user who has the followers.
     */
    @Transactional
    public void deleteAllFollowingMap(String fromUserId) {
        try {
            mappingsRepository.deleteByFromUserId(fromUserId);
            log.info("All following maps deleted successfully.");
        } catch (Exception ex) {
            log.error("Error deleting all following maps: {}", ex.getMessage(), ex);
            throw new ServiceException("Error deleting all following maps.", ex);
        }
    }

//    /**
//     * Gets a paginated list of users that the specified user is following.
//     *
//     * @param fromUserId The user whose followings are to be retrieved.
//     * @param page Page number.
//     * @param size Number of items per page.
//     * @return A paginated list of user followings.
//     */
//    public Page<UserFollowings> getFollowingList(String fromUserId, Integer page, Integer size) {
//        try {
//            Pageable paging = PageRequest.of(page, size);
//            return mappingsRepository.findAllByFromUserId(fromUserId, paging);
//        } catch (Exception ex) {
//            log.error("Error getting following list: {}", ex.getMessage(), ex);
//            throw new ServiceException("Error getting following list.", ex);
//        }
//    }

    /**
     * Retrieves paginated followings for a user.
     *
     * @param fromUserId The user ID to retrieve followings for.
     *  page       The page number (starting from 0).
     * size       The number of items per page.
     * @return A list of paginated followings.
     */
    public Page<UserFollowings> getFollowings(String fromUserId, Integer page, Integer size) {
        try {
            // Set default values if not provided
            page = (page != null) ? page : 0;
            size = (size != null) ? size : 100;

            Pageable pageable = PageRequest.of(page, size, Sort.by("id")); // Assuming "id" is the field you want to sort by
            return mappingsRepository.findAllByFromUserId(fromUserId, pageable);
        } catch (Exception ex) {
            log.error("Error fetching paginated followings: {}", ex.getMessage(), ex);
            throw new ServiceException("Error fetching paginated followings.", ex);
        }
    }



    public UserFollowings getFollowingRelationship(String fromUserId, String toUserId) {
        Optional<UserFollowings> existingRelationship = mappingsRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
        return existingRelationship.orElse(null);
    }

}
