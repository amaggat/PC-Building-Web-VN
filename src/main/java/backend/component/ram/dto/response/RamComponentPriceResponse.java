package backend.component.ram.dto.response;

import backend.component.common.dto.response.ComponentPriceResponse;
import backend.component.psu.entity.PsuComponentPrice;
import backend.component.ram.entity.RamComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RamComponentPriceResponse extends ComponentPriceResponse {

    private String ram;

    public RamComponentPriceResponse(RamComponentPrice ramComponentPrice) {
        super(ramComponentPrice);
        this.ram = ramComponentPrice.getRam().getId();
    }
}
