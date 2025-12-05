package com.example.product.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final String username;
    private final String password;
    private final String role;

    public CustomUserDetailsService(
            @Value("${app.security.user.username}") String username,
            @Value("${app.security.user.password}") String password,
            @Value("${app.security.user.role}") String role
    ) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!this.username.equals(username)) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return new User(
                this.username,
                this.password,
                List.of(new SimpleGrantedAuthority("ROLE_" + this.role))
        );
    }
}
