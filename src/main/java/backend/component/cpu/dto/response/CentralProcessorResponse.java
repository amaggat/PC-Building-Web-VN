package backend.component.cpu.dto.response;


import backend.component.common.ElectronicComponentsResponse;
import backend.component.cpu.entity.CentralProcessor;
import backend.component.cpu.entity.CpuPriceList;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.CpuRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentralProcessorResponse extends ElectronicComponentsResponse {

    private String socket;

    private Integer cores;

    private Integer Threads;

    private Integer minPrice;

    private Integer numberOfRating;

    private Double averageRating;

    private List<CpuPriceListResponse> priceList = new ArrayList<>();

    private List<PcProfile> pcProfileList = new ArrayList<>();

    private List<CpuRating> ratingList = new ArrayList<>();

    public CentralProcessorResponse(CentralProcessor centralProcessor) {
        super(centralProcessor);
        this.socket = centralProcessor.getSocket();
        this.cores = centralProcessor.getCores();
        this.Threads = centralProcessor.getThreads();
        this.minPrice = centralProcessor.getMinPrice();
        this.numberOfRating = centralProcessor.getNumberOfRating();
        this.averageRating = centralProcessor.getAverageRating();
        for (CpuPriceList cpuPriceList : centralProcessor.getPriceList()) {
            this.priceList.add(new CpuPriceListResponse(cpuPriceList));
        }
        this.pcProfileList = new ArrayList<>();
        this.ratingList = new ArrayList<>();
    }
}
