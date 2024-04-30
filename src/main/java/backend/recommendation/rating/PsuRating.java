package backend.recommendation.rating;

import backend.component.psu.PowerSupplyUnit;
import backend.recommendation.score.Rating;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "psu_rating")
public class PsuRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "psuid")
    private PowerSupplyUnit psu;

    public String getPsu() {
        return psu.getId();
    }

    public void setPsu(PowerSupplyUnit psu) {
        this.psu = psu;
    }
}
