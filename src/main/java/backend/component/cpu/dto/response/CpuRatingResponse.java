package backend.component.cpu.dto.response;

import backend.component.common.dto.response.RatingResponse;
import backend.recommendation.rating.CpuRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpuRatingResponse extends RatingResponse {

    private String centralProcessor;

    public CpuRatingResponse(CpuRating cpuRating) {
        super(cpuRating);
        this.centralProcessor = cpuRating.getCentralProcessor().getId();
    }
}
