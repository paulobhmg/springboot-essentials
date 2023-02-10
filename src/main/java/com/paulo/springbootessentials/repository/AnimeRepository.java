package com.paulo.springbootessentials.repository;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.utility.ListGenerator;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Repository
@NoArgsConstructor
public class AnimeRepository {

    public List<Anime> listAll() {
        return new ArrayList<>(ListGenerator.getAnimeList());
    }

    public Anime findById(long id) {
        return listAll().stream()
                .filter(anime -> anime.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found by id."));
    }
}
