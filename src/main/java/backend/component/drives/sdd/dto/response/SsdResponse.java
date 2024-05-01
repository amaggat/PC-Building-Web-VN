package backend.component.drives.sdd.dto.response;

import backend.component.common.dto.response.ElectronicComponentsResponse;
import backend.component.drives.sdd.entity.SolidStateDrive;
import backend.component.drives.sdd.entity.SsdComponentPrice;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.SsdRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsdResponse extends ElectronicComponentsResponse {

    private String storage;

    private List<SsdComponentPriceResponse> priceList = new ArrayList<>();

    private List<String> pcProfileList = new ArrayList<>();

    private List<SsdRatingResponse> ratingList = new ArrayList<>();

    public SsdResponse(SolidStateDrive solidStateDrive) {
        super(solidStateDrive);
        this.storage = solidStateDrive.getStorage();

        //add price list
        for(SsdComponentPrice ssdComponentPrice : solidStateDrive.getPriceList()) {
            this.priceList.add(new SsdComponentPriceResponse(ssdComponentPrice));
        }

        //set number of rating
        super.setNumberOfRating(solidStateDrive.getSsdRatingList().size());

        //set average rating
        if (solidStateDrive.getSsdRatingList().isEmpty()) {
            super.setAverageRating(null);
        } else {
            double avg = 0.0;
            for (SsdRating obj : solidStateDrive.getSsdRatingList()) {
                avg += obj.getRating();
            }
            avg = avg / solidStateDrive.getSsdRatingList().size();
            super.setAverageRating(Utility.to2DecimalDouble(avg));
        }

        //set min price
        for (SsdComponentPrice ssdComponentPrice : solidStateDrive.getPriceList()) {
            this.priceList.add(new SsdComponentPriceResponse(ssdComponentPrice));
            if(ssdComponentPrice.getPrice() < super.getMinPrice() || super.getMinPrice().equals(-1)) {
                super.setMinPrice(ssdComponentPrice.getPrice());
            }
        }

        //set pc profile use
        for (PcProfile pcProfile : solidStateDrive.getPcProfile()) {
            this.pcProfileList.add(pcProfile.getId());
        }

        //set gpu rating
        for(SsdRating ssdRating : solidStateDrive.getSsdRatingList()) {
            ratingList.add(new SsdRatingResponse(ssdRating));
        }
    }
}
