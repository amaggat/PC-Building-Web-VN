package backend.component.ram.dto.response;

import backend.component.common.dto.response.ElectronicComponentsResponse;
import backend.component.psu.entity.PowerSupplyUnit;
import backend.component.psu.entity.PsuComponentPrice;
import backend.component.ram.entity.Ram;
import backend.component.ram.entity.RamComponentPrice;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.PsuRating;
import backend.recommendation.rating.RamRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RamResponse extends ElectronicComponentsResponse {

    private Integer clockSpeed;

    private String sizeOfRam = new String();

    private List<RamComponentPriceResponse> priceList = new ArrayList<>();

    private List<String> pcProfileList = new ArrayList<>();

    private List<RamRatingResponse> ratingList = new ArrayList<>();

    public RamResponse(Ram ram) {
        super(ram);
        this.clockSpeed = ram.getClockSpeed();
        this.sizeOfRam = ram.getSizeOfRam();

        //add price list
        for(RamComponentPrice ramComponentPrice : ram.getPriceList()) {
            this.priceList.add(new RamComponentPriceResponse(ramComponentPrice));
        }

        //set number of rating
        super.setNumberOfRating(ram.getRamRatingList().size());

        //set pc profile use
        for (PcProfile pcProfile : ram.getPcProfileList()) {
            this.pcProfileList.add(pcProfile.getId());
        }

        //set item rating
        for(RamRating ramRating : ram.getRamRatingList()) {
            ratingList.add(new RamRatingResponse(ramRating));
        }

        //set average rating
        if (ram.getRamRatingList().isEmpty()) {
            super.setAverageRating(null);
        } else {
            double avg = 0.0;
            for (RamRating obj : ram.getRamRatingList()) {
                avg += obj.getRating();
            }
            avg = avg / ram.getRamRatingList().size();
            super.setAverageRating(Utility.to2DecimalDouble(avg));
        }

        //set min price
        for (RamComponentPrice ramComponentPrice : ram.getPriceList()) {
            this.priceList.add(new RamComponentPriceResponse(ramComponentPrice));
            if(ramComponentPrice.getPrice() < super.getMinPrice() || super.getMinPrice().equals(-1)) {
                super.setMinPrice(ramComponentPrice.getPrice());
            }
        }
    }
}
