package backend.recommendation.rating;

import backend.component.drives.sdd.entity.SolidStateDrive;
import backend.recommendation.score.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ssd_rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsdRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "ssdid")
    private SolidStateDrive ssd;
}
