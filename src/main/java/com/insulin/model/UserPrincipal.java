package com.insulin.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;

/**
 * Class which is an Adapter between User class and UserDetails which is provided by Spring Security.
 * Maps the corresponding values from the user to the userDetails object.
 * The last four methods are returning true, as we are not placing restrictions, like locking accounts.
 */
public class UserPrincipal implements UserDetails {
    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    /**
     * Returns the privileges of the user, what rights does he have.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] userAuth = this.user.getAuthorities().getAuthorities();
        return stream(userAuth).map(SimpleGrantedAuthority::new).collect(toList());
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
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
