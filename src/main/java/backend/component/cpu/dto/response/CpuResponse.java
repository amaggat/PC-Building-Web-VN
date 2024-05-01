package backend.component.cpu.dto.response;


import backend.component.common.dto.response.ElectronicComponentsResponse;
import backend.component.cpu.entity.CentralProcessor;
import backend.component.cpu.entity.CpuComponentPrice;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.CpuRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpuResponse extends ElectronicComponentsResponse {

    private String socket;

    private Integer cores;

    private Integer threads;

    private List<CpuComponentPriceResponse> priceList = new ArrayList<>();

    private List<String> pcProfileList = new ArrayList<>();

    private List<CpuRatingResponse> ratingList = new ArrayList<>();

    public CpuResponse(CentralProcessor centralProcessor) {
        super(centralProcessor);
        this.socket = centralProcessor.getSocket();
        this.cores = centralProcessor.getCores();
        this.threads = centralProcessor.getThreads();
        super.setNumberOfRating(centralProcessor.getCpuRatingList().size());

        if (centralProcessor.getCpuRatingList().isEmpty()) {
            super.setAverageRating(null);
        } else {
            double avg = 0.0;
            for (CpuRating obj : centralProcessor.getCpuRatingList()) {
                avg += obj.getRating();
            }
            avg = avg / centralProcessor.getCpuRatingList().size();
            super.setAverageRating(Utility.to2DecimalDouble(avg));
        }

        for (CpuComponentPrice cpuPriceList : centralProcessor.getPriceList()) {
            this.priceList.add(new CpuComponentPriceResponse(cpuPriceList));
            if(cpuPriceList.getPrice() < super.getMinPrice() || super.getMinPrice().equals(-1)) {
                super.setMinPrice(cpuPriceList.getPrice());
            }
        }

        for (PcProfile pcProfile : centralProcessor.getPcProfileList()) {
            this.pcProfileList.add(pcProfile.getId());
        }

        for(CpuRating cpuRating : centralProcessor.getCpuRatingList()) {
            ratingList.add(new CpuRatingResponse(cpuRating));
        }
    }
}
