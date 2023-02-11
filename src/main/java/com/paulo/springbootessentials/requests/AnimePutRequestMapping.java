package com.paulo.springbootessentials.requests;

import lombok.Data;

@Data
public class AnimePutRequestMapping {
    private long id;
    private String name;
    private int numberOfEpisodes;
}
