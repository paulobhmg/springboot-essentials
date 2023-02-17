package com.paulo.springbootessentials.utility;

import com.paulo.springbootessentials.domain.Anime;

public abstract class AnimeFactory {

    public static Anime createAnimeToBeSaved(){
        return new Anime("Bleach");
    }

    public static Anime createValidAnime() {
        return Anime.builder()
                .name("Bleach")
                .id(1L)
                .build();
    }

    public static Anime createUpdatedAnime(){
        return Anime.builder()
                .id(1L)
                .name("Shingeki no kyogin")
                .build();
    }
}
