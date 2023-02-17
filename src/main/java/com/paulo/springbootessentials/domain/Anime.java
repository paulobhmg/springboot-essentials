package com.paulo.springbootessentials.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotEmpty(message = "Anime name cannot be empty or null")
    private String name;

    @Column(name = "number_of_episodes")
    @Min(value = 0, message = "cannot be < 0")
    private int numberOfEpisodes;

    public Anime(String name) {
        this.name = name;
    }
}
