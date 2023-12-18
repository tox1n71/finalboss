package ru.itmo.finalboss.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmo.finalboss.repositories.UserRepo;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepo repo;

    @Bean
    public MyUserDetailsService myUserDetailsService(){
        return new MyUserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return Optional.of(repo.findByUsername(username)) // тут мб ошибка так как моя репа возрващает юзера а не опшионал
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
            @Override
            public UserDetails loadUserById(Long userId) {
                return repo.findById(userId)
                        .orElse(null);
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }


}
