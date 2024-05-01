package backend.component.psu.dto.response;

import backend.component.common.dto.response.ComponentPriceResponse;
import backend.component.psu.entity.PsuComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsuComponentPriceResponse extends ComponentPriceResponse {

    private String psu;

    public PsuComponentPriceResponse(PsuComponentPrice psuComponentPrice) {
        super(psuComponentPrice);
        this.psu = psuComponentPrice.getPsu().getId();
    }
}
