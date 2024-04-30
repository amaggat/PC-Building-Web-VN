package backend.component.cpu.dto.response;

import backend.component.cpu.entity.CpuPriceList;
import backend.retailer.Retailer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpuPriceListResponse {

    private int id;

    private String centralProcessor;

    private Retailer retailer;

    private String link;

    private int price;

    public CpuPriceListResponse(CpuPriceList cpuPriceList) {
        this.id = cpuPriceList.getId();
        this.centralProcessor = cpuPriceList.getCentralProcessor().getId();
        this.retailer = cpuPriceList.getRetailer();
        this.link = cpuPriceList.getLink();
        this.price = cpuPriceList.getPrice();
    }
}
