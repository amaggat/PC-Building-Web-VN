package backend.component.drives.hdd.dto.response;

import backend.component.common.dto.response.ElectronicComponentsResponse;
import backend.component.drives.hdd.entity.HardDiskDrive;
import backend.component.drives.hdd.entity.HddComponentPrice;
import backend.component.ram.entity.RamComponentPrice;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.HddRating;
import backend.recommendation.rating.RamRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HddResponse extends ElectronicComponentsResponse {

    private String storage;

    private List<HddComponentPriceResponse> priceList = new ArrayList<>();

    private List<String> pcProfileList = new ArrayList<>();

    private List<HddRatingResponse> ratingList = new ArrayList<>();

    public HddResponse(HardDiskDrive hardDiskDrive) {
        super(hardDiskDrive);
        this.storage = hardDiskDrive.getStorage();

        //add price list
        for(HddComponentPrice hddComponentPrice : hardDiskDrive.getPriceList()) {
            this.priceList.add(new HddComponentPriceResponse(hddComponentPrice));
        }

        //set number of rating
        super.setNumberOfRating(hardDiskDrive.getHddRatingList().size());

        //set average rating
        if (hardDiskDrive.getHddRatingList().isEmpty()) {
            super.setAverageRating(null);
        } else {
            double avg = 0.0;
            for (HddRating obj : hardDiskDrive.getHddRatingList()) {
                avg += obj.getRating();
            }
            avg = avg / hardDiskDrive.getHddRatingList().size();
            super.setAverageRating(Utility.to2DecimalDouble(avg));
        }

        //set min price
        for (HddComponentPrice hddComponentPrice : hardDiskDrive.getPriceList()) {
            this.priceList.add(new HddComponentPriceResponse(hddComponentPrice));
            if(hddComponentPrice.getPrice() < super.getMinPrice() || super.getMinPrice().equals(-1)) {
                super.setMinPrice(hddComponentPrice.getPrice());
            }
        }

        //set pc profile use
        for (PcProfile pcProfile : hardDiskDrive.getPcProfile()) {
            this.pcProfileList.add(pcProfile.getId());
        }

        //set gpu rating
        for(HddRating hddRating : hardDiskDrive.getHddRatingList()) {
            ratingList.add(new HddRatingResponse(hddRating));
        }
    }
}
