package com.paulo.springbootessentials.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePutRequestMapping {
    @NotBlank(message = "Id can't be null or empty")
    @Min(value = 1L, message = "Anime can't be minus than 1.")
    private long id;

    @NotBlank(message = "Name can't be null.")
    private String name;

    private int numberOfEpisodes;
}
