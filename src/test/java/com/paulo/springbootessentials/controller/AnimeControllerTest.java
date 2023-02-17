package com.paulo.springbootessentials.controller;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.service.AnimeService;
import com.paulo.springbootessentials.utility.AnimeFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeService;

    @BeforeEach
    void setup() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeFactory.createValidAnime()));
        BDDMockito.when(animeService.listAll(ArgumentMatchers.any())).thenReturn(animePage);
        BDDMockito.when(animeService.listAll()).thenReturn(animePage.toList());
    }

    @Test
    void test() {
        String expectedName = AnimeFactory.createValidAnime().getName();
        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }
}