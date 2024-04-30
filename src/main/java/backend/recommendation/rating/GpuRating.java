package backend.recommendation.rating;

import backend.component.gpu.GraphicProcessor;
import backend.recommendation.score.Rating;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "gpu_rating")
public class GpuRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "gpuid")
    private GraphicProcessor graphicProcessor;

    public String getGraphicProcessor() {
        return graphicProcessor.getId();
    }

    public void setGraphicProcessor(GraphicProcessor graphicProcessor) {
        this.graphicProcessor = graphicProcessor;
    }
}
