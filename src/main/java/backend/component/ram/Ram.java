package backend.component.ram;


import backend.component.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.RamRating;
import backend.utility.Utility;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "ram")
public class Ram extends ElectronicComponents {

    @Column(name = "clockspeed")
    @NotEmpty
    private Integer clockSpeed;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ram")
    private List<PcProfile> pcProfileList;

    @Column(name = "sizeofram")
    @NotEmpty
    private String sizeOfRam = new String();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ram", fetch = FetchType.EAGER)
    private List<RamPriceList> PriceList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ram")
    private List<RamRating> ramRatingList;

    @Transient
    private Optional<RamRating> ramRating;

    public Optional<RamRating> getRamRating() {
        return ramRating;
    }

    public void setRamRating(Optional<RamRating> ramRating) {
        this.ramRating = ramRating;
    }

    public Integer getClockSpeed() {
        return clockSpeed;
    }

    public void setClockSpeed(Integer clockSpeed) {
        this.clockSpeed = clockSpeed;
    }

    public String getSizeOfRam() {
        return sizeOfRam;
    }

    public void setSizeOfRam(String sizeOfRam) {
        this.sizeOfRam = sizeOfRam;
    }

    public List<RamPriceList> getPriceList() {
        return PriceList;
    }

    public void setPriceList(List<RamPriceList> ramPriceList) {
        this.PriceList = ramPriceList;
    }

    public List<String> getPcProfileList() {
        return Utility.returnPcProfileID(this.pcProfileList);
    }

    public void setPcProfileList(List<PcProfile> pcProfileList) {
        this.pcProfileList = pcProfileList;
    }

//    public List<RamRating> getRamRatingList() {
//        return ramRatingList;
//    }

    public void setRamRatingList(List<RamRating> ramRatingList) {
        this.ramRatingList = ramRatingList;
    }

    @Override
    public Double getAverageRating() {

        if (ramRatingList.isEmpty()) {
            return null;
        } else {
            double avg = 0.0;
            for (RamRating obj : this.ramRatingList) {
                avg += obj.getRating();
            }
            avg = avg / this.ramRatingList.size();
            return Utility.to2DecimalDouble(avg);
        }

    }

    @Override
    public Integer getNumberOfRating() {
        return this.ramRatingList.size();
    }

    public int getMinPrice(){
        int min = 500000000;
        for(RamPriceList ramPriceList : this.PriceList)
        {
            if(ramPriceList.getPrice() < min) {
                min = ramPriceList.getPrice();
            }
        }
        return min;
    }
}
