package com.paulo.springbootessentials.utility;

import com.paulo.springbootessentials.domain.Anime;

import java.util.List;

public abstract class ListGenerator {
    public static List<Anime> getAnimeList() {
        return List.of(new Anime(1L,"DBz"), new Anime(2L,"Yugi Oh"), new Anime(3L,"Bleach"), new Anime(4L,"Dota2"));
    }
}
