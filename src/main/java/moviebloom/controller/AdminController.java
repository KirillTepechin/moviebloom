package moviebloom.controller;

import moviebloom.domain.Actor;
import moviebloom.domain.Movie;
import moviebloom.dto.MovieDto;
import moviebloom.service.ActorService;
import moviebloom.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.MultipartConfigElement;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private ActorService actorService;

    //MOVIE CONTROLLERS

    @PostMapping(path = "/movie")
    public MovieDto createMovie(@Valid @RequestBody MovieDto movieDto) throws IOException {
        return new MovieDto(movieService.createMovie(movieDto));
    }
    @PutMapping(path = "/movie/{id}")
    public MovieDto updateMovie(@PathVariable Long id,@Valid @RequestBody MovieDto movieDto) throws IOException {
        return new MovieDto(movieService.updateMovie(id,movieDto));
    }
    @PutMapping(path = "movie/{id}/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    public Movie upload(@PathVariable("id") Long id, @RequestPart("poster") MultipartFile file) throws IOException {
        return movieService.uploadPhotoForMovie(file, id);
    }

    @DeleteMapping("/movie/{id}")
    public Long deleteMovie(@PathVariable("id") Movie movie) {
        return movieService.deleteMovie(movie);
    }

    //ACTOR CONTROLLERS

    @PutMapping(value="/actor/{id}", consumes = {MULTIPART_FORM_DATA_VALUE})
    public Actor updateActor(
            @PathVariable Long id,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestPart(value = "photo", required = false) MultipartFile file
    ) throws IOException {
        return actorService.updateActor(id, firstName, lastName, file);
    }


    @PostMapping(value = "/actor", consumes = {MULTIPART_FORM_DATA_VALUE})
    public Actor createActor(@RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestPart(value = "photo", required = false) MultipartFile file) throws IOException {
        return actorService.createActor(firstName,lastName, file);
    }

    @DeleteMapping("/actor/{id}")
    public Actor deleteActor(@PathVariable("id") Actor actor) {
        return actorService.deleteActor(actor);
    }

    //BEANS FOR UPLOAD

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement("");
    }

    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(2097152);
        return multipartResolver;
    }

}
