package backend.component.psu.dto.response;

import backend.component.common.dto.response.RatingResponse;
import backend.recommendation.rating.MainboardRating;
import backend.recommendation.rating.PsuRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsuRatingResponse extends RatingResponse {

    private String psu;

    public PsuRatingResponse(PsuRating PsuRating) {
        super(PsuRating);
        this.psu = PsuRating.getPsu().getId();
    }
}
