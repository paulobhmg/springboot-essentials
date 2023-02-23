package com.paulo.springbootessentials.integration;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.mapper.AnimeMapper;
import com.paulo.springbootessentials.repository.AnimeRepository;
import com.paulo.springbootessentials.requests.AnimePostRequestMapping;
import com.paulo.springbootessentials.requests.AnimePutRequestMapping;
import com.paulo.springbootessentials.utility.AnimeFactory;
import com.paulo.springbootessentials.utility.AnimeRequestFactory;
import com.paulo.springbootessentials.wrapper.PageableResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Log4j2
class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AnimeRepository animeRepository;

    private final Anime ANIME_TO_BE_SAVED = AnimeFactory.createAnimeToBeSaved();
    private final Anime VALID_ANIME = AnimeFactory.createValidAnime();
    private final List<Anime> EMPTY_LIST = Collections.emptyList();
    private final PageImpl<Anime> EMPTY_PAGE = new PageImpl<>(EMPTY_LIST);

    @Test
    @DisplayName("Tests if list method returns a not empty page list.")
    void listPageable_returnsANotEmptyPageList_whenSuccessful() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVED);
        log.info(savedAnime);
        Page<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();
        log.info(animePage);
       // Assertions.assertThat(animePage).isNotNull();
       // Assertions.assertThat(animePage.toList()).isNotEmpty();
    }

    @Test
    @DisplayName("Tests if listPageable method returns an empty page list")
    void listPageable_returnsAnEmptyPageList_whenSuccessful() {
        Page<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();
        Assertions.assertThat(animePage).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests if listNonPageable method returns a not empty list")
    void listNonPageable_returnsANotEmptyList_whenSuccessful() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVED);
        String expectedName = savedAnime.getName();
        List<Anime> animeList = testRestTemplate.exchange("/animes/list", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Tests if listNonPageable method returns an empty list when there is not anime to list")
    void listNonPageable_returnsAnEmptyList_whenThereIsNotAnimeToList() {
        List<Anime> animeList = testRestTemplate.exchange("/animes/list", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests if find anime by id")
    void findById_returnsAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVED);
        long expectedId = savedAnime.getId();
        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Tests if is throwing exception when anime not found by id")
    void findById_checkIfExceptionIsThrows_whenAnimeNotFoundById() {
        long expectedId = 2L;
        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);
        Assertions.assertThat(anime.getId()).isNotEqualTo(expectedId);
    }

    @Test
    @DisplayName("Tests if returns an anime find by name")
    void findByName_returnsAnimeList_whenSuccessful() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVED);
        String expectedName = savedAnime.getName();
        String url = String.format("/animes/search?name=%s", expectedName);
        List<Anime> animeList = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Tests if returns an empty list when anime not found by name")
    void findByName_returnsAnEmptyList_whenAnimeNotFoundByName() {
        List<Anime> animeList = testRestTemplate.exchange("/animes/search?name=invalid", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests anime persists")
    void save_persistsANewAnime_whenSuccessful() {
        ResponseEntity<Anime> exchange = testRestTemplate.exchange("/animes", HttpMethod.POST,
                new HttpEntity<>(ANIME_TO_BE_SAVED), Anime.class);
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(exchange.getBody()).isNotNull().isEqualTo(VALID_ANIME);
    }

    @Test
    @DisplayName("Tests anime replace")
    void replace_updatesAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVED);
        ResponseEntity<Anime> exchange = testRestTemplate.exchange("/animes", HttpMethod.PUT,
                new HttpEntity<>(AnimeRequestFactory.createAnimePutRequestMapping()), Anime.class);
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Tests anime delete")
    void delete_removesAnime_whenSuccessfull() {
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVED);
        ResponseEntity<Void> exchange = testRestTemplate.exchange("/animes/{id}", HttpMethod.DELETE, null,
                Void.class, VALID_ANIME.getId());
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
