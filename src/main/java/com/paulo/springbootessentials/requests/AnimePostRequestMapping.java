package com.paulo.springbootessentials.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePostRequestMapping {

    @NotBlank(message = "Name cannot be empty or null.")
    private String name;

    @Min(value = 0, message = "Number of episodes cannot be minus than 0.")
    private int numberOfEpisodes;

    @URL(message = "invalid uri")
    private String uri;
}
