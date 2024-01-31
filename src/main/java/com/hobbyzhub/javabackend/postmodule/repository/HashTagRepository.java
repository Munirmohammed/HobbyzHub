package com.hobbyzhub.javabackend.postmodule.repository;


import com.hobbyzhub.javabackend.postmodule.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag,String> {
}
