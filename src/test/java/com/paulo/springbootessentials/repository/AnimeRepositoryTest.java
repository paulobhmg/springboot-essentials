package com.paulo.springbootessentials.repository;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.mapper.AnimeMapper;
import com.paulo.springbootessentials.requests.AnimePostRequestMapping;
import com.paulo.springbootessentials.utility.AnimeFactory;
import com.paulo.springbootessentials.utility.AnimeRequestFactory;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Anime repository persistent tests")
@Log4j2
class AnimeRepositoryTest {

    @Autowired
    AnimeRepository animeRepository;

    private final Anime ANIME_TO_BE_SAVE = AnimeFactory.createAnimeToBeSaved();
    private final Anime VALID_ANIME = AnimeFactory.createValidAnime();

    @Test
    @DisplayName("Test anime persists when successful POST.")
    void save_persistAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVE);
        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThat(savedAnime.getId()).isGreaterThan(0);
        Assertions.assertThat(savedAnime.getName()).isEqualTo(VALID_ANIME.getName());
    }

    @Test
    @DisplayName("Test if is throws ConstraintViolationException when anime name is empty or null.")
    void save_checkIfConstraintIsValid_whenAnimeNameIsEmptyOrNull() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVE);
        savedAnime.setName(null);
        Assertions.assertThat(savedAnime.getName()).isNullOrEmpty();
    }

    @Test
    @DisplayName("Test empty list when anime not found by name.")
    void findByName_checkIfReturnsEmptyList_whenAnimeNotFoundByName() {
        List<Anime> animeList = List.of(animeRepository.save(ANIME_TO_BE_SAVE));
        String animeName = "Shingeki no kyogin";
        Assertions.assertThat(animeRepository.findByName(animeName)).isEmpty();
    }

    @Test
    @DisplayName("Test if is throws ConstraintViolationException when anime number of episodes is < 0.")
    void save_checkIfConstraintIsValid_whenAnimeEpisodeNumberIsMinusThanZero() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVE);
        savedAnime.setNumberOfEpisodes(-1);
        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThat(savedAnime.getNumberOfEpisodes()).isLessThan(1);
    }

    @Test
    @DisplayName("Test replace anime that already exists when successful PUT.")
    void replace_replaceAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVE);
        String originalName = savedAnime.getName();
        animeRepository.findById(savedAnime.getId());
        savedAnime.setName("Shingeki no kyogin");
        Anime updatedAnime = animeRepository.save(savedAnime);
        Assertions.assertThat(updatedAnime).isNotNull();
        Assertions.assertThat(updatedAnime.getId()).isGreaterThan(0);
        Assertions.assertThat(updatedAnime.getName()).isNotEqualTo(originalName);
    }

    @Test
    @DisplayName("Test remove anime when successful DELETE.")
    void delete_updateAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVE);
        Assertions.assertThat(savedAnime.getId()).isGreaterThan(0);

        Optional<Anime> animeFound = animeRepository.findById(savedAnime.getId());
        Assertions.assertThat(animeFound).isNotEmpty().contains(savedAnime);

        Anime animeToDelete = animeFound.get();
        animeRepository.delete(animeToDelete);

        Optional<Anime> animeDeleted = animeRepository.findById(animeToDelete.getId());
        Assertions.assertThat(animeDeleted).isEmpty();
    }
}