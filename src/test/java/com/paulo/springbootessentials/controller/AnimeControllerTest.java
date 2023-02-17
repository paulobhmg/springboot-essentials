package com.paulo.springbootessentials.controller;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.exception.ObjectNotFoundException;
import com.paulo.springbootessentials.requests.AnimePostRequestMapping;
import com.paulo.springbootessentials.requests.AnimePutRequestMapping;
import com.paulo.springbootessentials.service.AnimeService;
import com.paulo.springbootessentials.utility.AnimeFactory;
import com.paulo.springbootessentials.utility.AnimeRequestFactory;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Log4j2
class AnimeControllerTest {
    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeService;

    private final Anime ANIME_TO_BE_SAVE = AnimeFactory.createAnimeToBeSaved();
    private final Anime VALID_ANIME = AnimeFactory.createValidAnime();
    private final Anime UPDATED_ANIME = AnimeFactory.createUpdatedAnime();
    private final List<Anime> EMPTY_LIST = Collections.emptyList();
    private final PageImpl<Anime> EMPTY_PAGE = new PageImpl<>(EMPTY_LIST);

    @BeforeEach
    void setup() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(VALID_ANIME));
        BDDMockito.when(animeService.listAll(ArgumentMatchers.any())).thenReturn(animePage);
        BDDMockito.when(animeService.listAll()).thenReturn(animePage.toList());
        BDDMockito.when(animeService.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(VALID_ANIME));
        BDDMockito.when(animeService.findByIdOrThrowsBadRequest(ArgumentMatchers.anyLong()))
                .thenReturn(VALID_ANIME);
        BDDMockito.when(animeService.save(ArgumentMatchers.any(AnimePostRequestMapping.class)))
                .thenReturn(VALID_ANIME);
        BDDMockito.doNothing().when(animeService).replace(ArgumentMatchers.any(AnimePutRequestMapping.class));
        BDDMockito.doNothing().when(animeService).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("Tests if list method returns a not empty page list.")
    void listPageable_returnsANotEmptyPageList_whenSuccessful() {
        String expectedName = VALID_ANIME.getName();
        Page<Anime> animePage = animeController.listPageable(null).getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Tests if listPageable method returns an empty page list")
    void listPageable_returnsAnEmptyPageList_whenSuccessful() {
        BDDMockito.when(animeService.listAll(ArgumentMatchers.any())).thenReturn(EMPTY_PAGE);
        Page<Anime> animePage = animeController.listPageable(null).getBody();
        Assertions.assertThat(animePage).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests if listNonPageable method returns a not empty list")
    void listNonPageable_returnsANotEmptyList_whenSuccessful() {
        String expectedName = VALID_ANIME.getName();
        List<Anime> animeList = animeController.listNonPageable().getBody();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Tests if listNonPageable method returns an empty list when there is not anime to list")
    void listNonPageable_returnsAnEmptyList_whenThereIsNotAnimeToList() {
        BDDMockito.when(animeService.listAll()).thenReturn(EMPTY_LIST);
        List<Anime> animeList = animeController.listNonPageable().getBody();
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests if find anime by id")
    void findById_returnsAnime_whenSuccessful() {
        long expectedId = VALID_ANIME.getId();
        Anime anime = animeController.findByIdOrThrowsException(expectedId).getBody();
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Tests if is throwing exception when anime not found by id")
    void findById_checkIfExceptionIsThrows_whenAnimeNotFoundById() {
        BDDMockito.when(animeService.findByIdOrThrowsBadRequest(ArgumentMatchers.anyLong()))
                .thenReturn(null);

        Assertions.assertThatThrownBy(() -> animeController.findByIdOrThrowsException(1L))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    @DisplayName("Tests if returns an anime find by name")
    void findByName_returnsAnimeList_whenSuccessful() {
        String expectedName = VALID_ANIME.getName();
        List<Anime> animeList = animeController.findByName("teste").getBody();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Tests if returns an empty list when anime not found by name")
    void findbyName_returnsAnEmptyList_whenAnimeNotFoundByName() {
        BDDMockito.when(animeService.findByName(ArgumentMatchers.anyString()))
                .thenReturn(EMPTY_LIST);
        List<Anime> animeList = animeController.findByName("Teste").getBody();
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests persiste anime")
    void save_persistsANewAnime_whenSuccessful() {
        Anime anime = animeController.save(AnimeRequestFactory.createAnimePostRequestMapping()).getBody();
        Assertions.assertThat(anime).isNotNull().isEqualTo(VALID_ANIME);
    }

    @Test
    @DisplayName("Tests replace anime")
    void replace_updatesAnime_whenSuccessful() {
        Assertions.assertThatCode(() -> animeController.replace(AnimeRequestFactory.createAnimePutRequestMapping()))
                        .doesNotThrowAnyException();
        ResponseEntity<Void> entity = animeController.replace(AnimeRequestFactory.createAnimePutRequestMapping());
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Testes delete anime")
    void delete_removesAnime_whenSuccessfull() {
        Assertions.assertThatCode(() -> animeController.delete(1L)).doesNotThrowAnyException();
        ResponseEntity<Void> entity = animeController.delete(1L);
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}