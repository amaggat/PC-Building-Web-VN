package backend.component.psu.dto.response;

import backend.component.common.dto.response.ComponentPriceResponse;
import backend.component.mainboard.entity.MainboardComponentPrice;
import backend.component.psu.entity.PsuComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsuComponentPriceResponse extends ComponentPriceResponse {

    private String mainboard;

    public PsuComponentPriceResponse(PsuComponentPrice psuComponentPrice) {
        super(psuComponentPrice);
        this.mainboard = psuComponentPrice.getPsu().getId();
    }
}
