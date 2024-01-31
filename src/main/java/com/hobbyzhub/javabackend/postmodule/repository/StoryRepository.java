package com.hobbyzhub.javabackend.postmodule.repository;/*
*
@author ameda
@project backend-modulith
@package com.hobbyzhub.javabackend.postmodule.repository
*
*/

import com.hobbyzhub.javabackend.postmodule.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story,String> {
}
