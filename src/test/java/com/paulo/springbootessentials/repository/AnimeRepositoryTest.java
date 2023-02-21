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

import java.util.List;
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
        Anime savedAnime = animeRepository.save(AnimeFactory.createAnimeToBeSaved());
        Anime animeBuilder = AnimeFactory.createValidAnime();
        Assertions.assertThat(animeBuilder).isNotNull();
        Assertions.assertThat(animeBuilder.getId()).isGreaterThan(0);
        Assertions.assertThat(animeBuilder.getName()).isEqualTo(savedAnime.getName());
    }

    @Test
    @DisplayName("Test if is throws ConstraintViolationException when anime name is empty or null.")
    void save_checkIfConstraintIsValid_whenAnimeNameIsEmptyOrNull() {
        Anime savedAnime = animeRepository.save(AnimeMapper.INSTANCE.toAnime(AnimeRequestFactory.createAnimePostRequestMapping()));
        savedAnime.setName(null);
        Assertions.assertThat(savedAnime.getName()).isNullOrEmpty();
    }

    @Test
    @DisplayName("Test empty list when anime not found by name.")
    void findByName_checkIfReturnsEmptyList_whenAnimeNotFoundByName() {
        List<Anime> animeList = List.of(animeRepository.save(AnimeFactory.createAnimeToBeSaved()));
        String animeName = "Shingeki no kyogin";
        Assertions.assertThat(animeRepository.findByName(animeName)).isEmpty();
    }

    @Test
    @DisplayName("Test if is throws ConstraintViolationException when anime number of episodes is < 0.")
    void save_checkIfConstraintIsValid_whenAnimeEpisodeNumberIsMinusThanZero() {
        Anime savedAnime = animeRepository.save(AnimeMapper.INSTANCE.toAnime(AnimeRequestFactory.createAnimePostRequestMapping()));
        savedAnime.setNumberOfEpisodes(-1);
        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThat(savedAnime.getNumberOfEpisodes()).isLessThan(1);
    }

    @Test
    @DisplayName("Test replace anime that already exists when successful PUT.")
    void replace_replaceAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeFactory.createAnimeToBeSaved());
        Optional<Anime> animeFound = animeRepository.findById(savedAnime.getId());
        Assertions.assertThat(animeFound).isNotEmpty();

        Anime animeToUpdate = animeFound.get();
        animeToUpdate.setName("Shingeki no kyogin");
        Anime animeUpdated = animeRepository.save(animeToUpdate);

        Assertions.assertThat(animeUpdated).isNotNull();
        Assertions.assertThat(animeUpdated.getId()).isEqualTo(animeFound.get().getId());
        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeToUpdate.getName());
    }

    @Test
    @DisplayName("Test remove anime when successful DELETE.")
    void delete_updateAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeFactory.createAnimeToBeSaved());
        Assertions.assertThat(savedAnime.getId()).isGreaterThan(0);

        Optional<Anime> animeFound = animeRepository.findById(savedAnime.getId());
        Assertions.assertThat(animeFound).isNotEmpty().contains(savedAnime);

        Anime animeToDelete = animeFound.get();
        animeRepository.delete(animeToDelete);

        Optional<Anime> animeDeleted = animeRepository.findById(animeToDelete.getId());
        Assertions.assertThat(animeDeleted).isEmpty();
    }
}