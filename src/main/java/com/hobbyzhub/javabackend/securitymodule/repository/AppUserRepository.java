package com.hobbyzhub.javabackend.securitymodule.repository;

import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
    Optional<AppUser> findUserByEmail(String email);
    boolean existsByEmail(String email);
}
