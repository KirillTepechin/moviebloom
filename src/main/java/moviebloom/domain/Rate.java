package moviebloom.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import moviebloom.domain.id.RateId;

import javax.persistence.*;
import javax.swing.text.View;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Entity
@IdClass(RateId.class)
public class Rate{
    @Id
    @ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    @JsonIdentityReference(alwaysAsId = true)
    private Movie movie;
    @Id
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIdentityReference(alwaysAsId = true)
    private User user;
    private Float rate;

    @PostUpdate
    @PostPersist
    @PostRemove
    void countAverageMovieRate(){
        movie.setAverageRate(movie.getRates()!=null?
                movie.getRates().stream().mapToDouble(Rate::getRate).average()
                        .orElse(0):0);
    }
}
