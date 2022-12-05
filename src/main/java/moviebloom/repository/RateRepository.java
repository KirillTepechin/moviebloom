package moviebloom.repository;

import moviebloom.domain.Rate;
import moviebloom.domain.id.RateId;
import org.springframework.data.repository.CrudRepository;

public interface RateRepository extends CrudRepository<Rate, RateId> {
}
