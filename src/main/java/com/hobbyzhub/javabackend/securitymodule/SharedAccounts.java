package com.hobbyzhub.javabackend.securitymodule;

import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.service.AppUserService;
import com.hobbyzhub.javabackend.securitymodule.util.def.EntityModelMapper;
import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SharedAccounts extends EntityModelMapper {
    @Autowired
    private AppUserService appUserService;

    public SharedAccountsInformation retrieveSharedAccount(String userId) {
        AppUser appUser = appUserService.findUserById(userId);
        SharedAccountsInformation response = (SharedAccountsInformation) super.mapEntityToPayload(appUser);

        return response;
    }
}
