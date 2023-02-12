package com.paulo.springbootessentials.service;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.exception.AnimeNotFoundException;
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
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found by ID."));
    }

    public Anime findByNameOrThrowsBadRequest(String name) {
        return animeRepository.findByName(name)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found by name."));
    }

    @Transactional(rollbackFor = Exception.class)
    public Anime save(AnimePostRequestMapping animePostRequestMapping) throws Exception {
        animeRepository.save(animeMapper.toAnime(animePostRequestMapping));
        throw new Exception("Error");
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
