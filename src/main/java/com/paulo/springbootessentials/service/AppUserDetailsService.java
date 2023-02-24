package com.paulo.springbootessentials.service;

import com.paulo.springbootessentials.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(appUserRepository.findUserByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
