package com.insulin.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class which is an Adapter between User class and UserDetails which is provided by Spring Security.
 * Maps the corresponding values from the user to the userDetails object.
 * The last four methods are returning true, as we are not placing restrictions, like locking accounts.
 */
public class UserPrincipal implements UserDetails {
    private User user;
    private boolean noActiveCaptcha = true;

    public UserPrincipal(User user) {
        this.user = user;
    }

    /**
     * Returns the privilege of the user, what rights does he have.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority((user.getRole().toUpperCase())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return noActiveCaptcha;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setNoActiveCaptcha(boolean captcha) {
        this.noActiveCaptcha = captcha;
    }

    public boolean isNoActiveCaptcha() {
        return noActiveCaptcha;
    }
}
