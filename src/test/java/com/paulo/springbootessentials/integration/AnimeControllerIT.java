package com.paulo.springbootessentials.integration;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.domain.AppUser;
import com.paulo.springbootessentials.repository.AnimeRepository;
import com.paulo.springbootessentials.repository.AppUserRepository;
import com.paulo.springbootessentials.utility.AnimeFactory;
import com.paulo.springbootessentials.utility.AnimeRequestFactory;
import com.paulo.springbootessentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {

    @Autowired
    @Qualifier("testRestTemplateWithSimpleUserRole")
    private TestRestTemplate testRestTemplateWithSimpleUserRole;

    @Autowired
    @Qualifier("testRestTemplateWithAdminUserRole")
    private TestRestTemplate testRestTemplateWithAdminUserRole;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    private final Anime ANIME_TO_BE_SAVED = AnimeFactory.createAnimeToBeSaved();
    private final Anime VALID_ANIME = AnimeFactory.createValidAnime();

    private static final AppUser DEVELOPMENT_USER = AppUser.builder()
            .name("Development user")
            .username("dev")
            .password("{bcrypt}$2a$10$Oz6GIwnC.9uq0fPomU.In.5hew95aUegg/9Ow78h5d8XMAFEB9z46")
            .authorities("ROLE_USER")
            .isEnabled(true)
            .build();
    private static final AppUser ADMIN_USER = AppUser.builder()
            .name("Administrator user")
            .username("admin")
            .password("{bcrypt}$2a$10$MdcvmHx0DomS8eYWzOR8QuHWXcdNV3eVwElMFBvUWYqZoM4X8ksl.")
            .authorities("ROLE_USER, ROLE_ADMIN")
            .isEnabled(true)
            .build();

    @TestConfiguration
    @Lazy
    static class Config {

        @Bean("testRestTemplateWithSimpleUserRole")
        public TestRestTemplate testRestTemplateWithSimpleUserRole(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("dev", "test");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean("testRestTemplateWithAdminUserRole")
        public TestRestTemplate testRestTemplateWithAdminUserRole(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("admin", "admin");
            return new TestRestTemplate((restTemplateBuilder));
        }

    }

    @Test
    @DisplayName("Tests if list method returns a not empty page list.")
    void listPageable_returnsANotEmptyPageList_whenSuccessful() {
        appUserRepository.save(DEVELOPMENT_USER);
        animeRepository.save(ANIME_TO_BE_SAVED);
        Page<Anime> animePage = testRestTemplateWithSimpleUserRole.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
    }

    @Test
    @DisplayName("Tests if listPageable method returns an empty page list")
    void listPageable_returnsAnEmptyPageList_whenSuccessful() {
        appUserRepository.save(DEVELOPMENT_USER);
        Page<Anime> animePage = testRestTemplateWithSimpleUserRole.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();
        Assertions.assertThat(animePage).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests if listNonPageable method returns a not empty list")
    void listNonPageable_returnsANotEmptyList_whenSuccessful() {
        appUserRepository.save(DEVELOPMENT_USER);
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVED);
        String expectedName = savedAnime.getName();
        List<Anime> animeList = testRestTemplateWithSimpleUserRole.exchange("/animes/list", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Tests if listNonPageable method returns an empty list when there is not anime to list")
    void listNonPageable_returnsAnEmptyList_whenThereIsNotAnimeToList() {
        appUserRepository.save(DEVELOPMENT_USER);
        List<Anime> animeList = testRestTemplateWithSimpleUserRole.exchange("/animes/list", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests if find anime by id")
    void findById_returnsAnime_whenSuccessful() {
        appUserRepository.save(DEVELOPMENT_USER);
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVED);
        long expectedId = savedAnime.getId();
        Anime anime = testRestTemplateWithSimpleUserRole.getForObject("/animes/{id}", Anime.class, expectedId);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Tests if is throwing exception when anime not found by id")
    void findById_checkIfExceptionIsThrows_whenAnimeNotFoundById() {
        appUserRepository.save(DEVELOPMENT_USER);
        long expectedId = 2L;
        Anime anime = testRestTemplateWithSimpleUserRole.getForObject("/animes/{id}", Anime.class, expectedId);
        Assertions.assertThat(anime.getId()).isNotEqualTo(expectedId);
    }

    @Test
    @DisplayName("Tests if returns an anime find by name")
    void findByName_returnsAnimeList_whenSuccessful() {
        appUserRepository.save(DEVELOPMENT_USER);
        Anime savedAnime = animeRepository.save(ANIME_TO_BE_SAVED);
        String expectedName = savedAnime.getName();
        String url = String.format("/animes/search?name=%s", expectedName);
        List<Anime> animeList = testRestTemplateWithSimpleUserRole.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Tests if returns an empty list when anime not found by name")
    void findByName_returnsAnEmptyList_whenAnimeNotFoundByName() {
        appUserRepository.save(DEVELOPMENT_USER);
        List<Anime> animeList = testRestTemplateWithSimpleUserRole.exchange("/animes/search?name=invalid", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests anime persists")
    void save_persistsANewAnime_whenSuccessful() {
        appUserRepository.save(ADMIN_USER);
        ResponseEntity<Anime> exchange = testRestTemplateWithAdminUserRole.exchange("/animes", HttpMethod.POST,
                new HttpEntity<>(ANIME_TO_BE_SAVED), Anime.class);
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(exchange.getBody()).isNotNull().isEqualTo(VALID_ANIME);
    }

    @Test
    @DisplayName("Tests anime replace")
    void replace_updatesAnime_whenSuccessful() {
        appUserRepository.save(ADMIN_USER);
        animeRepository.save(ANIME_TO_BE_SAVED);
        ResponseEntity<Anime> exchange = testRestTemplateWithAdminUserRole.exchange("/animes", HttpMethod.PUT,
                new HttpEntity<>(AnimeRequestFactory.createAnimePutRequestMapping()), Anime.class);
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Tests anime delete")
    void delete_removesAnime_whenSuccessfull() {
        appUserRepository.save(ADMIN_USER);
        animeRepository.save(ANIME_TO_BE_SAVED);
        ResponseEntity<Void> exchange = testRestTemplateWithAdminUserRole.exchange("/animes/{id}", HttpMethod.DELETE, null,
                Void.class, VALID_ANIME.getId());
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
