package backend.component.common.dto.response;

import backend.component.common.model.ElectronicComponents;
import backend.recommendation.score.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectronicComponentsResponse {

    private String id;

    private String manufacturer;

    private String serieName;

    private String chipset;

    private String image;

    private String fullname;

    private Category category;

    private Integer view;

    private Integer numberOfRating;

    private Double averageRating;

    private Integer minPrice = -1;

    public ElectronicComponentsResponse(ElectronicComponents electronicComponents) {
        this.id = electronicComponents.getId();
        this.manufacturer = electronicComponents.getManufacturer();
        this.serieName = electronicComponents.getSerieName();
        this.chipset = electronicComponents.getChipset();
        this.image = electronicComponents.getImage();
        this.fullname = electronicComponents.getFullname();
        this.category = electronicComponents.getCategory();
        this.view = electronicComponents.getView();
    }
}
