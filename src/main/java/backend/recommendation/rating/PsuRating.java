package backend.recommendation.rating;

import backend.component.psu.entity.PowerSupplyUnit;
import backend.recommendation.score.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "psu_rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsuRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "psuid")
    private PowerSupplyUnit psu;

}
