package com.hobbyzhub.javabackend.securitymodule.service;

import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.repository.AppUserRepository;
import com.hobbyzhub.javabackend.sharedutils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> userCredential = this.loadUserByEmail(email);
//        return userCredential
//            .map(UserDetailsImpl::new)
//            .orElseThrow(()-> new UsernameNotFoundException("Not found user with email " + email));
        return userCredential
                .map(user-> new UserDetailsImpl(user.getEmail(),user.getPassword(),user.getRoles()))
                .orElseThrow(()->new UsernameNotFoundException("Not found user with email "+email));
    }

    private Optional<AppUser> loadUserByEmail(String email) {
        return appUserRepository.findUserByEmail(email);
    }
}
