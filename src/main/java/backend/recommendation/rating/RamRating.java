package backend.recommendation.rating;

import backend.component.ram.Ram;
import backend.recommendation.score.Rating;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ram_rating")
public class RamRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "ramid")
    private Ram ram;

    public String getRam() {
        return ram.getId();
    }

    public void setRam(Ram ram) {
        this.ram = ram;
    }
}
