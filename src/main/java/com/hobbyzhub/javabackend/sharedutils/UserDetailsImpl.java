package com.hobbyzhub.javabackend.sharedutils;

import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.entity.UserRoles;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDetailsImpl implements UserDetails {
    private String email;
    private String password;
    private boolean isEnabled;
    private String userId;
    private boolean newAccount;
    private Set<UserRoles> roles;

    public UserDetailsImpl(AppUser appuser){
        this.email = appuser.getEmail();
        this.password = appuser.getPassword();
        this.userId = appuser.getUserId();
        this.isEnabled = appuser.isAccountActive();
        this.newAccount = appuser.isNewAccount();
        this.roles = appuser.getRoles();
    }
    public UserDetailsImpl(String email,String password,
                           Set<UserRoles> roles ){
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role-> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
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
