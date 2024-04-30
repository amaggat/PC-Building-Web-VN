package backend.recommendation.rating;

import backend.component.drives.SolidStateDrive.SolidStateDrive;
import backend.recommendation.score.Rating;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ssd_rating")
public class SsdRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "ssdid")
    private SolidStateDrive ssd;

    public String getSsd() {
        return ssd.getId();
    }

    public void setSsd(SolidStateDrive ssd) {
        this.ssd = ssd;
    }
}
