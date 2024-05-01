package backend.component.mainboard.dto.response;

import backend.component.common.dto.response.ComponentPriceResponse;
import backend.component.mainboard.entity.MainboardComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainboardComponentPriceResponse extends ComponentPriceResponse {

    private String mainboard;

    public MainboardComponentPriceResponse(MainboardComponentPrice mainboardComponentPrice) {
        super(mainboardComponentPrice);
        this.mainboard = mainboardComponentPrice.getMainboard().getId();
    }
}
