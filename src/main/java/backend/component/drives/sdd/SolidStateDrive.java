package backend.component.drives.sdd;


import backend.component.common.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.SsdRating;
import backend.utility.Utility;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "ssd")
public class SolidStateDrive extends ElectronicComponents {

    @Column(name = "storage")
    @NotEmpty
    private String storage;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ssd")
    private List<PcProfile> pcProfile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ssd", fetch = FetchType.EAGER)
    private List<SsdPriceList> PriceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ssd", fetch = FetchType.LAZY)
    private List<SsdRating> ssdRatingList;

    @Transient
    private Optional<SsdRating> ssdRating;

    public String getStorage() {
        return storage;
    }

    public void setStorage(String size) {
        this.storage = size;
    }

    public List<SsdPriceList> getPriceList() {
        return PriceList;
    }

    public void setPriceList(List<SsdPriceList> ssdPriceList) {
        this.PriceList = ssdPriceList;
    }

    @Override
    public Double getAverageRating() {

        if (ssdRatingList.isEmpty()) {
            return null;
        } else {
            double avg = 0.0;
            for (SsdRating obj : this.ssdRatingList) {
                avg += obj.getRating();
            }
            avg = avg / this.ssdRatingList.size();
            return Utility.to2DecimalDouble(avg);
        }

    }

    @Override
    public Integer getNumberOfRating() {
        return this.ssdRatingList.size();
    }

    public Optional<SsdRating> getSsdRating() {
        return ssdRating;
    }

    public void setSsdRating(Optional<SsdRating> ssdRating) {
        this.ssdRating = ssdRating;
    }

    @Transient
    private int minPrice;

    public int getMinPrice(){
        int min = 500000000;
        for(SsdPriceList ssdPriceList : this.PriceList)
        {
            if(ssdPriceList.getPrice() < min) {
                min = ssdPriceList.getPrice();
            }
        }
        return min;
    }
}
