package com.hobbyzhub.javabackend.postmodule.repository;


import com.hobbyzhub.javabackend.postmodule.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post,String> {
    List<Post> findByUserId(String userId);
}
