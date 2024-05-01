package backend.recommendation.rating;

import backend.component.cpu.entity.CentralProcessor;
import backend.recommendation.score.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cpu_rating")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CpuRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "cpuid")
    private CentralProcessor centralProcessor;

}
