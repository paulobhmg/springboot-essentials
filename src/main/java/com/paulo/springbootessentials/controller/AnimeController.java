package com.paulo.springbootessentials.controller;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.requests.AnimePostRequestMapping;
import com.paulo.springbootessentials.requests.AnimePutRequestMapping;
import com.paulo.springbootessentials.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("animes")
public class AnimeController {
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> list(Pageable pageable) {
        return ResponseEntity.ok(animeService.listAll(pageable));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Anime>> listAll() {
        return ResponseEntity.ok(animeService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anime> findByIdOrThrowsException(@PathVariable long id) {
        return new ResponseEntity<>(animeService.findByIdOrThrowsBadRequest(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Anime> findByNameOrThrowsException(@RequestParam String name) {
        return new ResponseEntity<>(animeService.findByNameOrThrowsBadRequest(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestMapping animePostRequestMapping){
        System.out.println(animePostRequestMapping.getName());
        System.out.println(animePostRequestMapping.getNumberOfEpisodes());
        return new ResponseEntity<>(animeService.save(animePostRequestMapping), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestMapping animePutRequestMapping) {
        animeService.replace(animePutRequestMapping);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
