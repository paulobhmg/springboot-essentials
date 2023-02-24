package com.paulo.springbootessentials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface AppUserRepository extends JpaRepository<AppUserRepository, Integer> {
    UserDetails findUserByUsername(String username);
}
