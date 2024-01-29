package com.hobbyzhub.javabackend.categoriesmodule.repository;

import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbyCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HobbyCategoryRepository extends JpaRepository<HobbyCategory, String> {
    @Query("SELECT c FROM HobbyCategory c WHERE LOWER(c.categoryName) LIKE LOWER(CONCAT('%',?1,'%'))")
    Page<HobbyCategory> findByCategoryName(String categoryName, Pageable page);
}
