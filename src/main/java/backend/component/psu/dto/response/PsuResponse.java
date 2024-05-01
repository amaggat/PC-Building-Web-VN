package backend.component.psu.dto.response;

import backend.component.common.dto.response.ElectronicComponentsResponse;
import backend.component.psu.entity.PowerSupplyUnit;
import backend.component.psu.entity.PsuComponentPrice;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.MainboardRating;
import backend.recommendation.rating.PsuRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsuResponse extends ElectronicComponentsResponse {

    private List<PsuComponentPriceResponse> priceList = new ArrayList<>();

    private List<String> pcProfileList = new ArrayList<>();

    private List<PsuRatingResponse> ratingList = new ArrayList<>();

    public PsuResponse(PowerSupplyUnit powerSupplyUnit) {
        super(powerSupplyUnit);


        //add price list
        for(PsuComponentPrice psuComponentPrice : powerSupplyUnit.getPriceList()) {
            this.priceList.add(new PsuComponentPriceResponse(psuComponentPrice));
        }

        //set number of rating
        super.setNumberOfRating(powerSupplyUnit.getPsuRatingList().size());

        //set average rating
        if (powerSupplyUnit.getPsuRatingList().isEmpty()) {
            super.setAverageRating(null);
        } else {
            double avg = 0.0;
            for (PsuRating obj : powerSupplyUnit.getPsuRatingList()) {
                avg += obj.getRating();
            }
            avg = avg / powerSupplyUnit.getPsuRatingList().size();
            super.setAverageRating(Utility.to2DecimalDouble(avg));
        }

        //set min price
        for (PsuComponentPrice gpuPriceList : powerSupplyUnit.getPriceList()) {
            this.priceList.add(new PsuComponentPriceResponse(gpuPriceList));
            if(gpuPriceList.getPrice() < super.getMinPrice() || super.getMinPrice().equals(-1)) {
                super.setMinPrice(gpuPriceList.getPrice());
            }
        }

        //set pc profile use
        for (PcProfile pcProfile : powerSupplyUnit.getPcProfileList()) {
            this.pcProfileList.add(pcProfile.getId());
        }

        //set gpu rating
        for(PsuRating psuRating : powerSupplyUnit.getPsuRatingList()) {
            ratingList.add(new PsuRatingResponse(psuRating));
        }
    }
}
