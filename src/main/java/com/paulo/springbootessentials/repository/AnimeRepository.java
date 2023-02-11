package com.paulo.springbootessentials.repository;

import com.paulo.springbootessentials.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

}
