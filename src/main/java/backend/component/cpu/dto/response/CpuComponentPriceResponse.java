package backend.component.cpu.dto.response;

import backend.component.common.dto.response.ComponentPriceResponse;
import backend.component.cpu.entity.CpuComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpuComponentPriceResponse extends ComponentPriceResponse {

    private String centralProcessor;

    public CpuComponentPriceResponse(CpuComponentPrice cpuPriceList) {
        super(cpuPriceList);
        this.centralProcessor = cpuPriceList.getCentralProcessor().getId();
    }
}
