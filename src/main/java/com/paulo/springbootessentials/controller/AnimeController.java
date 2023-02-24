package com.paulo.springbootessentials.controller;

import com.paulo.springbootessentials.domain.Anime;
import com.paulo.springbootessentials.requests.AnimePostRequestMapping;
import com.paulo.springbootessentials.requests.AnimePutRequestMapping;
import com.paulo.springbootessentials.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("animes")
@Log4j2
public class AnimeController {
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> listPageable(Pageable pageable) {
        return ResponseEntity.ok(animeService.listAll(pageable));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Anime>> listNonPageable() {
        return ResponseEntity.ok(animeService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anime> findByIdOrThrowsException(@PathVariable long id) {
        return new ResponseEntity<>(animeService.findByIdOrThrowsBadRequest(id), HttpStatus.OK);
    }

    @GetMapping("/byId/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> findByIdAuthenticationPrincipal(@PathVariable long id,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        log.info(userDetails);
        return new ResponseEntity<>(animeService.findByIdOrThrowsBadRequest(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name) {
        return new ResponseEntity<>(animeService.findByName(name), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestMapping animePostRequestMapping){
        return new ResponseEntity<>(animeService.save(animePostRequestMapping), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestMapping animePutRequestMapping) {
        animeService.replace(animePutRequestMapping);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
