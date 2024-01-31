package com.hobbyzhub.javabackend.sharedutils;

import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserDetailsImpl implements UserDetails {
    private String email;
    private String password;
    private boolean isEnabled;
    private String userId;
    private boolean newAccount;

    public UserDetailsImpl(AppUser appuser){
        this.email = appuser.getEmail();
        this.password = appuser.getPassword();
        this.userId = appuser.getUserId();
        this.isEnabled = appuser.isAccountActive();
        this.newAccount = appuser.isNewAccount();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
