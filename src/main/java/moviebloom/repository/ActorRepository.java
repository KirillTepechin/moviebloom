package moviebloom.repository;

import moviebloom.domain.Actor;
import moviebloom.domain.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActorRepository extends CrudRepository<Actor, Long> {
    @Override
    List<Actor> findAll();
}
