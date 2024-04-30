package backend.component.common;

import backend.recommendation.score.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectronicComponentsResponse {

    private String id = new String();

    private String manufacturer = new String();

    private String serieName = new String();

    private String chipset = new String();

    private String image = new String();

    private String fullname = new String();

    private Category category;

    private Integer view;

    private Double averageRating;

    private Integer numberOfRating;

}
