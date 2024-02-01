package com.hobbyzhub.javabackend.helprequestmodule;

import com.hobbyzhub.javabackend.helprequestmodule.entity.Help;
import com.hobbyzhub.javabackend.helprequestmodule.model.request.HelpRequest;
import com.hobbyzhub.javabackend.helprequestmodule.service.HelpService;
import com.hobbyzhub.javabackend.securitymodule.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/help")
public class HelpController {
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;
    private final HelpService helpService;
    private final AppUserService appUserService;
    @PostMapping("/get-help/{userId}")
    public ResponseEntity<?> seekHelp(@Valid @RequestBody HelpRequest request, @PathVariable("userId") String userId){
        Help help = helpService.seekHelp(request,userId);
        if(help!=null){
            return new ResponseEntity<>(help, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/requests")
    public ResponseEntity<?> gerRequests(){
        List<Help> requests = helpService.requests();
        if(!(requests.isEmpty())){
            return new ResponseEntity<>(requests,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/request/{helpId}")
    public ResponseEntity<?> requestPerId(@PathVariable("helpId") String helpId){
        Help help = helpService.requestPerId(helpId);
        if(help!=null){
            return new ResponseEntity<>(help,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/request/delete/{helpId}")
    public ResponseEntity<?> clearRequest(@PathVariable("helpId") String helpId){
        Map<String,Boolean> retrieved =helpService.deleteRequestById(helpId);
        return new ResponseEntity<>(retrieved,HttpStatus.OK);
    }

    @GetMapping("/data/{userId}")
    public ResponseEntity<?> getData(@PathVariable("userId") String userId){
        return new ResponseEntity<>( appUserService.findUserById(userId),
                HttpStatus.OK
        );
    }

    @GetMapping("/data")
    public ResponseEntity<?> getDataByEmail(@RequestParam("email") String email){
        return new ResponseEntity<>(
                appUserService.findUserByEmail(email),
                HttpStatus.OK
        );
    }

}
