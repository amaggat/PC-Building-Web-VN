package backend.component.mainboard.dto.response;

import backend.component.common.dto.response.ElectronicComponentsResponse;
import backend.component.mainboard.entity.Mainboard;
import backend.component.mainboard.entity.MainboardComponentPrice;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.MainboardRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainboardResponse extends ElectronicComponentsResponse {

    private String socket = new String();

    private String formFactor = new String();

    private String sizeOfRam = new String();

    private Integer memorySlot;

    private List<MainboardComponentPriceResponse> priceList = new ArrayList<>();

    private List<String> pcProfileList = new ArrayList<>();

    private List<MainboardRatingResponse> ratingList = new ArrayList<>();

    public MainboardResponse(Mainboard mainboard) {
        super(mainboard);
        this.socket = mainboard.getSocket();
        this.formFactor = mainboard.getFormFactor();
        this.sizeOfRam = mainboard.getSizeOfRam();
        this.memorySlot = mainboard.getMemorySlot();

        //add price list
        for(MainboardComponentPrice gpuPriceList : mainboard.getPriceList()) {
            this.priceList.add(new MainboardComponentPriceResponse(gpuPriceList));
        }

        //set number of rating
        super.setNumberOfRating(mainboard.getMainboardRatingList().size());

        //set pc profile use
        for (PcProfile pcProfile : mainboard.getPcProfileList()) {
            this.pcProfileList.add(pcProfile.getId());
        }

        //set item rating
        for(MainboardRating mainboardRating : mainboard.getMainboardRatingList()) {
            ratingList.add(new MainboardRatingResponse(mainboardRating));
        }

        //set average rating
        if (mainboard.getMainboardRatingList().isEmpty()) {
            super.setAverageRating(null);
        } else {
            double avg = 0.0;
            for (MainboardRating obj : mainboard.getMainboardRatingList()) {
                avg += obj.getRating();
            }
            avg = avg / mainboard.getMainboardRatingList().size();
            super.setAverageRating(Utility.to2DecimalDouble(avg));
        }

        //set min price
        for (MainboardComponentPrice gpuPriceList : mainboard.getPriceList()) {
            this.priceList.add(new MainboardComponentPriceResponse(gpuPriceList));
            if(gpuPriceList.getPrice() < super.getMinPrice() || super.getMinPrice().equals(-1)) {
                super.setMinPrice(gpuPriceList.getPrice());
            }
        }
    }
}
