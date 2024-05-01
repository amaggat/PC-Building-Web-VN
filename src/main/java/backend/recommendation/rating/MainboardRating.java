package backend.recommendation.rating;

import backend.component.mainboard.entity.Mainboard;
import backend.recommendation.score.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mainboard_rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainboardRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "mainid")
    private Mainboard mainboard;

}
