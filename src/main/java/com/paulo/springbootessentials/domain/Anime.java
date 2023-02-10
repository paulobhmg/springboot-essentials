package com.paulo.springbootessentials.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anime {
    private long id;
    private String name;
    private List<Episode> episodes;
    private int numberOfEpisodes;

    public Anime(String name) {
        this.id = ThreadLocalRandom.current().nextLong(1, 22020);
        this.name = name;
    }

    public Anime(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
