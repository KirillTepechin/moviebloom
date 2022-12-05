package moviebloom.repository;

import moviebloom.domain.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findTop20ByOrderByAverageRateDesc();
    Movie findTopByOrderByAverageRateDesc();
    @Override
    List<Movie> findAll();
}
