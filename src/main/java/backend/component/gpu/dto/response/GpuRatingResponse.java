package backend.component.gpu.dto.response;

import backend.component.common.dto.response.RatingResponse;
import backend.recommendation.rating.CpuRating;
import backend.recommendation.rating.GpuRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpuRatingResponse extends RatingResponse {

    private String centralProcessor;

    public GpuRatingResponse(GpuRating gpuRating) {
        super(gpuRating);
        this.centralProcessor = gpuRating.getGraphicProcessor().getId();
    }
}
