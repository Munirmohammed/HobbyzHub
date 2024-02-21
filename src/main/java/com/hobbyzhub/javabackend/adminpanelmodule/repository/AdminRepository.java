package com.hobbyzhub.javabackend.adminpanelmodule.repository;/*
*
@author ameda
@project backend-modulith
@package com.hobbyzhub.javabackend.adminpanelmodule.repository
*
*/

import com.hobbyzhub.javabackend.adminpanelmodule.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,String> {
}
