package moviebloom.domain.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import moviebloom.domain.Movie;
import moviebloom.domain.User;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RateId implements Serializable {
    private Long movie;
    private Long user;
}
