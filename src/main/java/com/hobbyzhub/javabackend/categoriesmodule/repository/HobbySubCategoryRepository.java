package com.hobbyzhub.javabackend.categoriesmodule.repository;

import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbyCategory;
import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbySubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HobbySubCategoryRepository extends JpaRepository<HobbySubCategory, String> {
    @Query("SELECT c FROM HobbySubCategory c WHERE LOWER(c.categoryName) LIKE LOWER(CONCAT('%',?1,'%'))")
    Page<HobbySubCategory> findByCategoryName(String categoryName, Pageable page);
    
    @Query("SELECT h FROM HobbySubCategory h WHERE h.hobbyCategory=?1")
    List<HobbySubCategory> findAllByHobbyCategory(HobbyCategory hobbyCategory);
}
