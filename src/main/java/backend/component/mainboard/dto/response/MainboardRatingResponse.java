package backend.component.mainboard.dto.response;

import backend.component.common.dto.response.RatingResponse;
import backend.recommendation.rating.GpuRating;
import backend.recommendation.rating.MainboardRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainboardRatingResponse extends RatingResponse {

    private String centralProcessor;

    public MainboardRatingResponse(MainboardRating mainboardRating) {
        super(mainboardRating);
        this.centralProcessor = mainboardRating.getMainboard().getId();
    }
}
