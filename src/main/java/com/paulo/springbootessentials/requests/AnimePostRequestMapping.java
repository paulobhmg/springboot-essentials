package com.paulo.springbootessentials.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class AnimePostRequestMapping {

    @NotBlank(message = "Name cannot be empty or null")
    private String name;

    private int numberOfEpisodes;

    @URL(message = "invalid uri")
    private String uri;
}
