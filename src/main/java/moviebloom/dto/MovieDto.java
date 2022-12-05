package moviebloom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import moviebloom.domain.Actor;
import moviebloom.domain.Movie;
import moviebloom.domain.Rate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto implements Serializable {
    public MovieDto(Movie movie){
        id = movie.getId();
        name = movie.getName();
        description = movie.getDescription();
        poster = movie.getPoster();
        year = movie.getYear();
        averageRate = movie.getAverageRate();
        genres = movie.getGenres().stream().map(Enum::name).collect(Collectors.toList());
        actorsIds = movie.getActors().stream().map(Actor::getId).collect(Collectors.toList());
        actors = movie.getActors();
    }
    private Long id;
    @NotNull
    private String name;
    private List<String> genres;
    private List<Long> actorsIds;
    private List<Actor> actors;
    private List<Rate> rates;
    @NotNull

    private Integer year;
    @NotNull

    private String description;
    private String poster;
    private double averageRate;
}
