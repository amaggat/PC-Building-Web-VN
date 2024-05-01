package backend.recommendation.rating;

import backend.component.drives.hdd.entity.HardDiskDrive;
import backend.recommendation.score.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "hdd_rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HddRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "hddid")
    private HardDiskDrive hdd;
}
