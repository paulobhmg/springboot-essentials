package com.paulo.springbootessentials.repository;

import com.paulo.springbootessentials.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
    Optional<Anime> findByName(String name);
}
