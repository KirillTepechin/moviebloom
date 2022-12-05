package moviebloom.domain;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import moviebloom.domain.enums.Genre;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "actors")
@ToString(exclude = "actors")
@RequiredArgsConstructor
@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Movie {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    @ElementCollection(targetClass=Genre.class)
    private List<Genre> genres = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private List<Actor> actors = new ArrayList<>();
    @OneToMany(mappedBy="movie", cascade = CascadeType.ALL)
    private List<Rate> rates;
    @Column(name = "year_of_release")
    @NonNull
    private Integer year;
    private String poster;
    @NonNull
    @Column(length = 1000)
    private String description;
    private double averageRate;

    public void addActor(Actor actor){
        if(actors.contains(actor)){
            return;
        }
        actors.add(actor);
        actor.addMovie(this);
    }

    @PreRemove
    private void preRemove() {
        for (Actor a : actors) {
            a.getMovies().remove(this);
        }
    }

}
