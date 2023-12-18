package ru.itmo.finalboss.security.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MyUserDetailsService extends UserDetailsService {
    UserDetails loadUserById(Long userId) throws UsernameNotFoundException;
}
