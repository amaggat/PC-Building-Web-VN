package backend.component.common;

import backend.recommendation.score.Rating;
import backend.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {

    private UserInfo user;

    private double rating;

    private boolean isFavorite = false;

    public RatingResponse(Rating rating) {
        this.rating = rating.getRating();
        this.isFavorite = rating.isFavorite();
        this.user = new UserInfo(rating.getUser().getId(), rating.getUser().getName());

    }
}
