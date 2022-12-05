package moviebloom.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.bytebuddy.utility.nullability.MaybeNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "movies")
@RequiredArgsConstructor
@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
public class Actor {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private String photo;
    @ManyToMany(mappedBy = "actors",fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Movie> movies;

    public void addMovie(Movie movie) {
        if(movies.contains(movie)){
            return;
        }
        movies.add(movie);
        movie.addActor(this);
    }

    @PreRemove
    private void preRemove(){
        for (Movie m : movies) {
            m.getActors().remove(this);
        }
    }
}
