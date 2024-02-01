package com.hobbyzhub.javabackend.securitymodule.repository;

import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
    Optional<AppUser> findUserByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM AppUser u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%',?1,'%'))")
    Page<AppUser> findByFullName(String name, Pageable page);
}
