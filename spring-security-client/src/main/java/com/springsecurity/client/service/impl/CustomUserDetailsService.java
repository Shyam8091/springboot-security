package com.springsecurity.client.service.impl;

import com.springsecurity.client.entity.User;
import com.springsecurity.client.repositry.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByFirstName(username);
        log.info("CustomUserDetailsService - loadUserByUsername - " + user);
        if (user == null) {
            throw new UsernameNotFoundException("Not Found");
        }
        return new CustomUserDetails(user);
    }
}
