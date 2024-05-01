package backend.component.drives.sdd.dto.response;

import backend.component.common.dto.response.ComponentPriceResponse;
import backend.component.drives.hdd.entity.HddComponentPrice;
import backend.component.drives.sdd.entity.SsdComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsdComponentPriceResponse extends ComponentPriceResponse {

    private String ssd;

    public SsdComponentPriceResponse(SsdComponentPrice ssdComponentPrice) {
        super(ssdComponentPrice);
        this.ssd = ssdComponentPrice.getSsd().getId();
    }
}
