package com.paulo.springbootessentials.mapper;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.requests.AnimePostRequestMapping;
import com.paulo.springbootessentials.requests.AnimePutRequestMapping;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-21T14:14:27-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.5 (Private Build)"
)
@Component
public class AnimeMapperImpl extends AnimeMapper {

    @Override
    public Anime toAnime(AnimePostRequestMapping animePostRequestMapping) {
        if ( animePostRequestMapping == null ) {
            return null;
        }

        String name = null;

        Anime anime = new Anime( name );

        return anime;
    }

    @Override
    public Anime toAnime(AnimePutRequestMapping animePutRequestMapping) {
        if ( animePutRequestMapping == null ) {
            return null;
        }

        String name = null;

        Anime anime = new Anime( name );

        return anime;
    }
}
