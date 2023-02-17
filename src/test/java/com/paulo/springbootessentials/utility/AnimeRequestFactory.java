package com.paulo.springbootessentials.utility;

import com.paulo.springbootessentials.requests.AnimePostRequestMapping;
import com.paulo.springbootessentials.requests.AnimePutRequestMapping;

public abstract class AnimeRequestFactory {
    public static AnimePostRequestMapping createAnimePostRequestMapping() {
        return AnimePostRequestMapping.builder()
                .name(AnimeFactory.createAnimeToBeSaved().getName())
                .build();
    }

    public static AnimePutRequestMapping createAnimePutRequestMapping() {
        return AnimePutRequestMapping.builder()
                .id(AnimeFactory.createUpdatedAnime().getId())
                .name(AnimeFactory.createUpdatedAnime().getName())
                .build();
    }
}
