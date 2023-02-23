package com.paulo.springbootessentials.service;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.exception.ObjectNotFoundException;
import com.paulo.springbootessentials.repository.AnimeRepository;
import com.paulo.springbootessentials.utility.AnimeFactory;
import com.paulo.springbootessentials.utility.AnimeRequestFactory;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeServiceMock;

    @Mock
    private AnimeRepository animeRepositoryMock;

    private final Anime VALID_ANIME = AnimeFactory.createValidAnime();
    private final Anime UPDATED_ANIME = AnimeFactory.createUpdatedAnime();
    private final List<Anime> EMPTY_LIST = Collections.emptyList();
    private final PageImpl<Anime> EMPTY_PAGE = new PageImpl<>(EMPTY_LIST);

    @BeforeEach
    void setup() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(VALID_ANIME));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(animePage);
        BDDMockito.when(animeRepositoryMock.findAll()).thenReturn(animePage.toList());
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(List.of(VALID_ANIME));
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(VALID_ANIME));
        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class))).thenReturn(VALID_ANIME);
        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("Tests if list method returns a not empty page list.")
    void listPageable_returnsANotEmptyPageList_whenSuccessful() {
        String expectedName = VALID_ANIME.getName();
        Page<Anime> animePage = animeServiceMock.listAll(PageRequest.of(0, 1));
        Assertions.assertThat(animePage).isNotNull().isNotEmpty();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Tests if listPageable method returns an empty page list")
    void listPageable_returnsAnEmptyPageList_whenSuccessful() {
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(EMPTY_PAGE);
        Page<Anime> animePage = animeServiceMock.listAll(PageRequest.of(0, 1));
        Assertions.assertThat(animePage).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests if listNonPageable method returns a not empty list")
    void listNonPageable_returnsANotEmptyList_whenSuccessful() {
        String expectedName = VALID_ANIME.getName();
        List<Anime> animeList = animeServiceMock.listAll();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Tests if listNonPageable method returns an empty list when there is not anime to list")
    void listNonPageable_returnsAnEmptyList_whenThereIsNotAnimeToList() {
        BDDMockito.when(animeRepositoryMock.findAll()).thenReturn(EMPTY_LIST);
        List<Anime> animeList = animeServiceMock.listAll();
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests if find anime by id")
    void findById_returnsAnime_whenSuccessful() {
        long expectedId = VALID_ANIME.getId();
        Anime anime = animeServiceMock.findByIdOrThrowsBadRequest(expectedId);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Tests if is throwing exception when anime not found by id")
    void findById_checkIfExceptionIsThrows_whenAnimeNotFoundById() {
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> animeServiceMock.findByIdOrThrowsBadRequest(1L))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    @DisplayName("Tests if returns an anime find by name")
    void findByName_returnsAnimeList_whenSuccessful() {
        String expectedName = VALID_ANIME.getName();
        List<Anime> animeList = animeServiceMock.findByName("test");
        Assertions.assertThat(animeList).isNotNull().isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Tests if returns an empty list when anime not found by name")
    void findByName_returnsAnEmptyList_whenAnimeNotFoundByName() {
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(EMPTY_LIST);
        List<Anime> animeList = animeServiceMock.findByName("Test");
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Tests anime persists")
    void save_persistsANewAnime_whenSuccessful() {
        Anime anime = animeServiceMock.save(AnimeRequestFactory.createAnimePostRequestMapping());
        Assertions.assertThat(anime).isNotNull().isEqualTo(VALID_ANIME);
    }

    @Test
    @DisplayName("Tests replace anime")
    void replace_updatesAnime_whenSuccessful() {
        Anime savedAnime = animeServiceMock.save(AnimeRequestFactory.createAnimePostRequestMapping());
        Assertions.assertThat(savedAnime).isNotNull();
        String originalName = savedAnime.getName();
        Assertions.assertThatCode(() -> animeServiceMock.replace(AnimeRequestFactory.createAnimePutRequestMapping()))
                .doesNotThrowAnyException();
        Assertions.assertThat(AnimeRequestFactory.createAnimePutRequestMapping().getId()).isEqualTo(UPDATED_ANIME.getId());
        Assertions.assertThat(AnimeRequestFactory.createAnimePutRequestMapping().getName()).isNotEqualTo(originalName);
    }

    @Test
    @DisplayName("Testes delete anime")
    void delete_removesAnime_whenSuccessfull() {
        Anime savedAnime = animeServiceMock.save(AnimeRequestFactory.createAnimePostRequestMapping());
        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThatCode(() -> animeRepositoryMock.delete(savedAnime)).doesNotThrowAnyException();
    }

}