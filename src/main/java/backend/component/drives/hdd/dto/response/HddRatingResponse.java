package backend.component.drives.hdd.dto.response;

import backend.component.common.dto.response.RatingResponse;
import backend.recommendation.rating.HddRating;
import backend.recommendation.rating.RamRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HddRatingResponse extends RatingResponse {

    private String psu;

    public HddRatingResponse(HddRating hddRating) {
        super(hddRating);
        this.psu = hddRating.getHdd().getId();
    }
}
