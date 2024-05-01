package backend.component.ram.dto.response;

import backend.component.common.dto.response.RatingResponse;
import backend.recommendation.rating.PsuRating;
import backend.recommendation.rating.RamRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RamRatingResponse extends RatingResponse {

    private String psu;

    public RamRatingResponse(RamRating ramRating) {
        super(ramRating);
        this.psu = ramRating.getRam().getId();
    }
}
