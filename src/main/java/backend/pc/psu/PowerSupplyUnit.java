package backend.pc.psu;

import backend.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.type.rating.PsuRating;
import backend.util.Utility;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;


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

    public List<PsuRating> getPsuRatingList() {
        return psuRatingList;
    }

    public void setPsuRatingList(List<PsuRating> psuRatingList) {
        this.psuRatingList = psuRatingList;
    }
}
