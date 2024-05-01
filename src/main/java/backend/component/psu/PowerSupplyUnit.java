package backend.component.psu;

import backend.component.common.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.PsuRating;
import backend.utility.Utility;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "psu")
public class PowerSupplyUnit extends ElectronicComponents {

    @Column(name = "power")
    @NotEmpty
    private Integer power;

    @Column(name = "standard_80")
    @NotEmpty
    private String standard_80 = new String();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "psu", fetch = FetchType.EAGER)
    private List<PsuPriceList> PriceList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "psu")
    private List<PcProfile> pcProfileList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "psu")
    private List<PsuRating> psuRatingList;

    @Transient
    Optional<PsuRating> psuRating;

    public Optional<PsuRating> getPsuRating() {
        return psuRating;
    }

    public void setPsuRating(Optional<PsuRating> psuRating) {
        this.psuRating = psuRating;
    }

    public List<PsuPriceList> getPriceList() {
        return PriceList;
    }

    public void setPriceList(List<PsuPriceList> psuPriceList) {
        this.PriceList = psuPriceList;
    }

    public String getStandard_80() {
        return standard_80;
    }

    public void setStandard_80(String standard_80) {
        this.standard_80 = standard_80;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        power = power;
    }

    public List<String> getPcProfileList() {
        return Utility.returnPcProfileID(this.pcProfileList);
    }

    public void setPcProfileList(List<PcProfile> pcProfileList) {
        this.pcProfileList = pcProfileList;
    }

//    public List<PsuRating> getPsuRatingList() {
//        return psuRatingList;
//    }

    public void setPsuRatingList(List<PsuRating> psuRatingList) {
        this.psuRatingList = psuRatingList;
    }

    @Override
    public Double getAverageRating() {

        if (psuRatingList.isEmpty()) {
            return null;
        } else {
            double avg = 0.0;
            for (PsuRating obj : this.psuRatingList) {
                avg += obj.getRating();
            }
            avg = avg / this.psuRatingList.size();
            return Utility.to2DecimalDouble(avg);
        }

    }

    @Override
    public Integer getNumberOfRating() {
        return this.psuRatingList.size();
    }

    public int getMinPrice(){
        int min = 500000000;
        for(PsuPriceList psuPriceList : this.PriceList)
        {
            if(psuPriceList.getPrice() < min) {
                min = psuPriceList.getPrice();
            }
        }
        return min;
    }
}
