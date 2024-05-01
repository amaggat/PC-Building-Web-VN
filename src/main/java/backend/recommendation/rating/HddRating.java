package backend.recommendation.rating;

import backend.component.drives.hdd.HardDiskDrive;
import backend.recommendation.score.Rating;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "hdd_rating")
public class HddRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "hddid")
    private HardDiskDrive hdd;

    public String getHdd() {
        return hdd.getId();
    }

    public void setHdd(HardDiskDrive hdd) {
        this.hdd = hdd;
    }
}
