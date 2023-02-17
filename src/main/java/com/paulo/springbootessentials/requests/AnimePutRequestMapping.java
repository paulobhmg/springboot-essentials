package com.paulo.springbootessentials.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePutRequestMapping {
    private long id;
    private String name;
    private int numberOfEpisodes;
}
