package com.hobbyzhub.javabackend.helprequestmodule.repository;

import com.hobbyzhub.javabackend.helprequestmodule.entity.Help;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface HelpRepository extends JpaRepository<Help,String> {
    Optional<Help> findByEmail(String email);
}
