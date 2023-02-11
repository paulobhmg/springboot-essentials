package com.paulo.springbootessentials.mapper;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.requests.AnimePostRequestMapping;
import com.paulo.springbootessentials.requests.AnimePutRequestMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    public abstract Anime toAnime(AnimePostRequestMapping animePostRequestMapping);
    public abstract Anime toAnime(AnimePutRequestMapping animePutRequestMapping);
}
