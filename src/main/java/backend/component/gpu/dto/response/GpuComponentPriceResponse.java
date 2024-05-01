package backend.component.gpu.dto.response;

import backend.component.common.dto.response.ComponentPriceResponse;
import backend.component.gpu.entity.GpuComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpuComponentPriceResponse extends ComponentPriceResponse {

    private String graphicProcessor;

    public GpuComponentPriceResponse(GpuComponentPrice gpuPriceList) {
        super(gpuPriceList);
        this.graphicProcessor = gpuPriceList.getGraphicProcessor().getId();
    }
}
