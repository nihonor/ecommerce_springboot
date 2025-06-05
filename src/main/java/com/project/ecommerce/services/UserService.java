package com.project.ecommerce.services;

import com.project.ecommerce.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user=userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Email Not Found")
        );

        return new User(
                user.getPassword(),
                user.getEmail(),
                Collections.emptyList()
        );
    }
}
