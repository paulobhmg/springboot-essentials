package com.paulo.springbootessentials.service;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.exception.ObjectNotFoundException;
import com.paulo.springbootessentials.mapper.AnimeMapper;
import com.paulo.springbootessentials.repository.AnimeRepository;
import com.paulo.springbootessentials.requests.AnimePostRequestMapping;
import com.paulo.springbootessentials.requests.AnimePutRequestMapping;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AnimeService {
    private AnimeRepository animeRepository;
    private AnimeMapper animeMapper;

    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    public Anime findByIdOrThrowsBadRequest(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Anime not found by ID."));
    }

    public Anime findByNameOrThrowsBadRequest(String name) {
        return animeRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Anime not found by name."));
    }

    @Transactional
    public Anime save(AnimePostRequestMapping animePostRequestMapping) {
        return animeRepository.save(animeMapper.toAnime(animePostRequestMapping));
    }

    @Transactional
    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowsBadRequest(id));
    }

    @Transactional
    public void replace(AnimePutRequestMapping animePutRequestMapping) {
        Anime animeSaved = findByIdOrThrowsBadRequest(animePutRequestMapping.getId());
        animePutRequestMapping.setId(animeSaved.getId());
        animeRepository.save(animeMapper.toAnime(animePutRequestMapping));
    }
}
