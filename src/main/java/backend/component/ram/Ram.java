package backend.component.ram;


import backend.component.common.model.ElectronicComponents;
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
    private List<RamComponentPrice> PriceList;

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

    public List<RamComponentPrice> getPriceList() {
        return PriceList;
    }

    public void setPriceList(List<RamComponentPrice> ramComponentPrice) {
        this.PriceList = ramComponentPrice;
    }

    public List<String> getPcProfileList() {
        return Utility.returnPcProfileID(this.pcProfileList);
    }

    public void setPcProfileList(List<PcProfile> pcProfileList) {
        this.pcProfileList = pcProfileList;
    }


    public void setRamRatingList(List<RamRating> ramRatingList) {
        this.ramRatingList = ramRatingList;
    }
}
