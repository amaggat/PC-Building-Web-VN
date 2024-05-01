package backend.recommendation.score;


import backend.user.User;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @Column(name = "rating")
    private double rating;

    @Column(name = "favorite")
    private boolean isFavorite = false;

}
