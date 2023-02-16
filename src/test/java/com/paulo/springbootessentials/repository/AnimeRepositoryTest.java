package com.paulo.springbootessentials.repository;

import com.paulo.springbootessentials.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@DisplayName("Anime repository persistent tests")
@Log4j2
class AnimeRepositoryTest {

    @Autowired
    AnimeRepository animeRepository;

    @Test
    @DisplayName("Test a new anime persist when successful POST.")
    void save_persistAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(createAnime());
        Anime animeBuilder = Anime.builder()
                .id(savedAnime.getId())
                .name(savedAnime.getName())
                .numberOfEpisodes(savedAnime.getNumberOfEpisodes())
                .build();
        Assertions.assertThat(animeBuilder).isNotNull();
        Assertions.assertThat(animeBuilder.getId()).isGreaterThan(0);
        Assertions.assertThat(animeBuilder.getName()).isEqualTo(savedAnime.getName());
    }

    @Test
    @DisplayName("Test replace anime that already exists when successful PUT.")
    void replace_replaceAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(createAnime());
        Optional<Anime> animeFound = animeRepository.findById(savedAnime.getId());
        Assertions.assertThat(animeFound).isNotEmpty();
        Anime animeToUpdate = Anime.builder()
                .id(animeFound.get().getId())
                .name("Bleach")
                .numberOfEpisodes(animeFound.get().getNumberOfEpisodes())
                .build();
        Anime animeUpdated = animeRepository.save(animeToUpdate);
        Assertions.assertThat(animeUpdated).isNotNull();
        Assertions.assertThat(animeUpdated.getId()).isEqualTo(animeFound.get().getId());
        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeToUpdate.getName());
    }

    @Test
    @DisplayName("Test remove anime when successful DELETE.")
    void delete_updateAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(createAnime());
        Assertions.assertThat(savedAnime.getId()).isGreaterThan(0);

        Optional<Anime> animeFound = animeRepository.findById(savedAnime.getId());
        Assertions.assertThat(animeFound).isNotEmpty();

        Anime animeToDelete = animeFound.get();
        animeRepository.delete(animeToDelete);

        Optional<Anime> animeDeleted = animeRepository.findById(animeToDelete.getId());
        Assertions.assertThat(animeDeleted).isEmpty();
    }

    private Anime createAnime() {
        return Anime.builder().name("shingeki").build();
    }
}