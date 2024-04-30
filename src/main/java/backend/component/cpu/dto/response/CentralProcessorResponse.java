package backend.component.cpu.dto.response;


import backend.component.common.ElectronicComponentsResponse;
import backend.component.cpu.entity.CpuPriceList;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.CpuRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentralProcessorResponse extends ElectronicComponentsResponse {

    private String socket = new String();

    private Integer cores;

    private Integer Threads;

    private int minPrice;

    private int numberOfRating;

    private Double averageRating;

    private List<CpuPriceList> priceList;

    private List<PcProfile> pcProfileList;

    private List<CpuRating> ratingList;

}
