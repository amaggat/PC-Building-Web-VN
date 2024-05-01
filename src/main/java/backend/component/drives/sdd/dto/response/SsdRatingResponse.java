package backend.component.drives.sdd.dto.response;

import backend.component.common.dto.response.RatingResponse;
import backend.recommendation.rating.HddRating;
import backend.recommendation.rating.SsdRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsdRatingResponse extends RatingResponse {

    private String psu;

    public SsdRatingResponse(SsdRating ssdRating) {
        super(ssdRating);
        this.psu = ssdRating.getSsd().getId();
    }
}
