package com.paulo.springbootessentials.repository;

import com.paulo.springbootessentials.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    AppUser findUserByUsername(String username);
}
