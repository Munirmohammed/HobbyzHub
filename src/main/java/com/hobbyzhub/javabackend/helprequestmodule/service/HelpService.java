package com.hobbyzhub.javabackend.helprequestmodule.service;
import com.hobbyzhub.javabackend.helprequestmodule.entity.Help;
import com.hobbyzhub.javabackend.helprequestmodule.exceptions.HelpNotFoundException;
import com.hobbyzhub.javabackend.helprequestmodule.model.request.HelpRequest;
import com.hobbyzhub.javabackend.helprequestmodule.repository.HelpRepository;
import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public final class HelpService {
    private final HelpRepository helpRepository;
    private final AppUserService appUserService;

    public String setId(){
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12);
    }
    public Help seekHelp(HelpRequest request, String userId) {

        AppUser response = appUserService.findUserById(userId);
        log.info("response:"+response);
        Help help = null;
        if(response==null){
            log.info("issue with establishing feign call...");
            return null;
        }
        log.info("feign call to retrieve user details successfully initiated...");
        help = Help.builder()
                    .helpId(setId())
                    .message(request.getMessage())
                    .userId(response.getUserId())
                    .profilePic(response.getProfileImage())
                    .fullName(request.getFullName())
                    .email(response.getEmail())
                    .build();
        helpRepository.save(help);
        log.info("successfully created a help request...");
        return help;
    }

    public List<Help> requests() {
       List<Help> helps = helpRepository.findAll();
       if(!(helps.isEmpty())){
           return helps;
       }else {
           throw new HelpNotFoundException("no help requests here...");
       }
    }

    public Help requestPerId(String helpId) {
        return helpRepository.findById(helpId)
                .orElseThrow(()->
                        new HelpNotFoundException("help request not found with passed id"+helpId));
    }

    public Map<String, Boolean> deleteRequestById(String helpId) {
        Help help = helpRepository.findById(helpId)
                .orElseThrow(()->
                        new HelpNotFoundException("Help request wasn't found for this id :: "+helpId));
        helpRepository.delete(help);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted.",true);
        return response;
    }
}
