package backend.component.drives.hdd.dto.response;

import backend.component.common.dto.response.ComponentPriceResponse;
import backend.component.drives.hdd.entity.HddComponentPrice;
import backend.component.ram.entity.RamComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HddComponentPriceResponse extends ComponentPriceResponse {

    private String hdd;

    public HddComponentPriceResponse(HddComponentPrice hddComponentPrice) {
        super(hddComponentPrice);
        this.hdd = hddComponentPrice.getHdd().getId();
    }
}
