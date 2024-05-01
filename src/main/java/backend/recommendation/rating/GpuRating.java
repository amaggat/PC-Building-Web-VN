package backend.recommendation.rating;

import backend.component.gpu.entity.GraphicProcessor;
import backend.recommendation.score.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "gpu_rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpuRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "gpuid")
    private GraphicProcessor graphicProcessor;
}
