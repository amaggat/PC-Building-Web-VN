package backend.component.gpu.dto.response;

import backend.component.common.dto.response.ElectronicComponentsResponse;
import backend.component.gpu.entity.GpuComponentPrice;
import backend.component.gpu.entity.GraphicProcessor;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.CpuRating;
import backend.recommendation.rating.GpuRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpuResponse extends ElectronicComponentsResponse {

    private Integer VRam;

    private List<GpuComponentPriceResponse> priceList = new ArrayList<>();

    private List<String> pcProfileList = new ArrayList<>();

    private List<GpuRatingResponse> ratingList = new ArrayList<>();

    public GpuResponse(GraphicProcessor graphicProcessor) {
        super(graphicProcessor);
        this.VRam = graphicProcessor.getVRam();

        //add price list
        for(GpuComponentPrice gpuPriceList : graphicProcessor.getPriceList()) {
            this.priceList.add(new GpuComponentPriceResponse(gpuPriceList));
        }

        //set number of rating
        super.setNumberOfRating(graphicProcessor.getGpuRatingList().size());

        //set pc profile use
        for (PcProfile pcProfile : graphicProcessor.getPcProfileList()) {
            this.pcProfileList.add(pcProfile.getId());
        }

        //set item rating
        for(GpuRating gpuRating : graphicProcessor.getGpuRatingList()) {
            ratingList.add(new GpuRatingResponse(gpuRating));
        }

        //set average rating
        if (graphicProcessor.getGpuRatingList().isEmpty()) {
            super.setAverageRating(null);
        } else {
            double avg = 0.0;
            for (GpuRating obj : graphicProcessor.getGpuRatingList()) {
                avg += obj.getRating();
            }
            avg = avg / graphicProcessor.getGpuRatingList().size();
            super.setAverageRating(Utility.to2DecimalDouble(avg));
        }

        //set min price
        for (GpuComponentPrice gpuPriceList : graphicProcessor.getPriceList()) {
            this.priceList.add(new GpuComponentPriceResponse(gpuPriceList));
            if(gpuPriceList.getPrice() < super.getMinPrice() || super.getMinPrice().equals(-1)) {
                super.setMinPrice(gpuPriceList.getPrice());
            }
        }
    }
}
