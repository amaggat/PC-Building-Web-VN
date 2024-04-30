package backend.component.cpu.dto.response;


import backend.component.common.ElectronicComponentsResponse;
import backend.component.cpu.entity.CpuPriceList;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.CpuRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentralProcessorResponse extends ElectronicComponentsResponse {

    private String socket = new String();

    private Integer cores;

    private Integer Threads;

    private int minPrice;

    private int numberOfRating;

    private List<CpuPriceList> PriceList;

    private List<PcProfile> pcProfileList;

    private List<CpuRating> cpuRatingList;

    private Optional<CpuRating> cpuRating;

    @Override
    public Double getAverageRating() {
        if (cpuRatingList.isEmpty()) {
            return null;
        } else {
            double avg = 0.0;
            for (CpuRating obj : this.cpuRatingList) {
                avg += obj.getRating();
            }
            avg = avg / this.cpuRatingList.size();
            return Utility.to2DecimalDouble(avg);
        }
    }

    @Override
    public Integer getNumberOfRating() {
        return this.cpuRatingList.size();
    }

    public int getMinPrice(){
        int min = 500000000;
        for(CpuPriceList cpuPriceList : this.PriceList)
        {
            if(cpuPriceList.getPrice() < min) {
                min = cpuPriceList.getPrice();
            }
        }
        return min;
    }
}
