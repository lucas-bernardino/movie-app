package com.movie.server.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.server.model.Comment;
import com.movie.server.model.User;
import com.movie.server.model.dto.IdUserMediaDto;
import com.movie.server.model.Media;
import com.movie.server.model.View;
import com.movie.server.service.AuthenticationService;
import com.movie.server.service.MediaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/media")
@Validated
public class MediaController {

    private final MediaService mediaService;
    private final AuthenticationService authenticationService;

    public MediaController(MediaService mediaService, AuthenticationService authenticationService) {
        this.mediaService = mediaService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("")
    public ResponseEntity<Void> createMedia(@RequestBody @Valid Media media) {
        mediaService.createMedia(media);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/newuser")
    public ResponseEntity<Void> addNewUserToMedia(@RequestBody @Valid IdUserMediaDto UserMediaObject) {
        mediaService.addUserToMedia(UserMediaObject.imdbId(), UserMediaObject.userId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    @JsonView(View.Default.class)
    public ResponseEntity<List<Media>> getAllMediasByUsername(@RequestHeader("Authorization") String token) {
        User user = authenticationService.getUser(token);
        List<Media> medias = mediaService.getAllMediasByUsername(user.getUsername());
        return ResponseEntity.ok().body(medias);
    }

    @GetMapping("/all")
    @JsonView(View.Test.class)
    public ResponseEntity<List<Media>> getAllMedias() {
        List<Media> medias = mediaService.getAllMedias();
        return ResponseEntity.ok().body(medias);
    }

    @GetMapping("/{id}")
    @JsonView(View.Default.class)
    public ResponseEntity<Media> getMediaByImdbId(@PathVariable String id) {
        Media media = mediaService.getMediaByImdbId(id);
        return ResponseEntity.ok().body(media);
    }

    @DeleteMapping("/{imdbId}")
    public ResponseEntity<Void> deleteMediaByImdbId(@PathVariable String imdbId, @RequestHeader("Authorization") String token) {
        mediaService.deleteMediaByImdbId(imdbId, token);
        return ResponseEntity.ok().build();
    }
}
