package backend.recommendation.rating;

import backend.component.ram.entity.Ram;
import backend.recommendation.score.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ram_rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RamRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "ramid")
    private Ram ram;
}
