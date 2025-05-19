package com.ryqg.jiaofu.config.security;

import cn.hutool.core.collection.CollectionUtil;
import com.ryqg.jiaofu.domain.model.UserCredentials;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private UserCredentials userCredentials;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return CollectionUtil.isNotEmpty(userCredentials.getRoles()) ?
                userCredentials.getRoles().stream().map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Override
    public String getPassword() {
        return userCredentials.getPassword();
    }

    @Override
    public String getUsername() {
        return userCredentials.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
