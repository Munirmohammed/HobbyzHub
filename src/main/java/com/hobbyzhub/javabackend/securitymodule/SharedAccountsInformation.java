package com.hobbyzhub.javabackend.securitymodule;

import com.hobbyzhub.javabackend.securitymodule.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class SharedAccountsInformation {
    @Autowired
    private AppUserService appUserService;
}
