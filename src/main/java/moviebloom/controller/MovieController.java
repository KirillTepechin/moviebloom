package moviebloom.controller;

import moviebloom.domain.Movie;
import moviebloom.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public Movie findMovie(@PathVariable Long id){
        return movieService.findOne(id);
    }
    @GetMapping
    public List<Movie> findAllMovies(){
        return movieService.findAll();
    }
    @GetMapping("/top")
    public List<Movie> list() {
        return movieService.findTop20();
    }
}
