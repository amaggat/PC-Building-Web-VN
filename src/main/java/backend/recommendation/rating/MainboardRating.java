package backend.recommendation.rating;

import backend.component.mainboard.Mainboard;
import backend.recommendation.score.Rating;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mainboard_rating")
public class MainboardRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "mainid")
    private Mainboard mainboard;

    public String getMainboard() {
        return mainboard.getId();
    }

    public void setMainboard(Mainboard mainboard) {
        this.mainboard = mainboard;
    }
}
