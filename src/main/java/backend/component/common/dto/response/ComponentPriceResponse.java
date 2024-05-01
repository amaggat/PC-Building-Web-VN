package backend.component.common.dto.response;

import backend.component.common.model.ComponentPrice;
import backend.retailer.Retailer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentPriceResponse {

    private int id;

    private Retailer retailer;

    private String link;

    private int price;

    public ComponentPriceResponse(ComponentPrice componentPrice) {
        this.id = componentPrice.getId();
        this.retailer = componentPrice.getRetailer();
        this.link = componentPrice.getLink();
        this.price = componentPrice.getPrice();
    }
}
