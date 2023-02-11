package com.paulo.springbootessentials.requests;

import lombok.Data;

@Data
public class AnimePostRequestMapping {
    private String name;
    private int numberOfEpisodes;
}
