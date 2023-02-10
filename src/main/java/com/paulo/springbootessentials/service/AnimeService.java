package com.paulo.springbootessentials.service;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.repository.AnimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnimeService {
    private AnimeRepository animeRepository;

    public List<Anime> listAll() {
        return animeRepository.listAll();
    }

    public Anime findById(long id) {
        return animeRepository.findById(id);
    }

    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }
}
