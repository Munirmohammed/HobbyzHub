package com.hobbyzhub.javabackend.followersmodule.repository;

import com.hobbyzhub.javabackend.followersmodule.entity.UserFollowings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 *
 * @author Munir Mohammed
 */

public interface MappingsRepository extends JpaRepository<UserFollowings, Integer> {
    /**
     * Deletes all following maps based on the 'fromUserId' variable
     * @param fromUserId
     */
	void deleteByFromUserId(String fromUserId);
	
	/**
	 * Retrieves a page of people I am following. The
	 * results are however not sorted
	 * @ pageable is the pageable type
	 * @return a page of UserFollowings entries with the number specified 
	 * 		   as the {@literal size}
	 */
	Page<UserFollowings> findAllByFromUserId(String fromUserId, Pageable pageable);
    
    /**
	 * Retrieves a page of people following me. The
	 * results are however not sorted
	 * @param pageable is the pageable type
	 * @return a page of UserFollowings entries with the number specified 
	 * 		   as the {@literal size}
	 */
    Page<UserFollowings> findAllByToUserId(String toUserId, Pageable pageable);
    
    /**
     * Deletes a single following entry
     * @param fromUserId is the id of the user following
     * @param toUserId is the id of the user being followed
     */
    @Query("delete from UserFollowings f where f.fromUserId=:fromUserId and f.toUserId=:toUserId")
    @Modifying
    void deleteFollowingMap(String fromUserId, String toUserId);

	Optional<UserFollowings> findByFromUserIdAndToUserId(String fromUserId, String toUserId);

	void deleteAllByToUserId(String toUserId);
}
