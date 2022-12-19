package com.tw.ecommerceplatform.entities;

import com.tw.ecommerceplatform.utility.RegistrationStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class SecurityUserDetails implements UserDetails {

    private final UserEntity user;

    public SecurityUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // Create a granted authority based on the user role
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName()));
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
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
        if (user.getStatus() == RegistrationStatus.APPROVED)
            return true;
        else if (user.getStatus() == RegistrationStatus.PENDING)
            return false;

        return true;
    }
}
