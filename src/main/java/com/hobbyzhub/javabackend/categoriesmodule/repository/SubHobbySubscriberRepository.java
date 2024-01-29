package com.hobbyzhub.javabackend.categoriesmodule.repository;

import com.hobbyzhub.javabackend.categoriesmodule.entity.SubHobbySubscriber;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubHobbySubscriberRepository extends JpaRepository<SubHobbySubscriber, String> {
    @Transactional
    void deleteByUserIdAndSubCategoryId(String userId, String subCategoryId);
    boolean existsByUserIdAndSubCategoryId(String userId, String subCategoryId);
    Page<SubHobbySubscriber> findByUserId(String userId, Pageable page);
    Page<SubHobbySubscriber> findBySubCategoryId(String subCategoryId, Pageable page);
}
