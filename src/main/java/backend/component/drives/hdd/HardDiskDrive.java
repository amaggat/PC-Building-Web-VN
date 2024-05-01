package backend.component.drives.hdd;

import backend.component.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.HddRating;
import backend.utility.Utility;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "hdd")
public class HardDiskDrive extends ElectronicComponents {

    @Column(name = "storage")
    @NotEmpty
    private String storage;

    @Transient
    Optional<HddRating> hddRating;

    @Transient
    private int minPrice;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "hdd")
    private List<PcProfile> pcProfile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hdd", fetch = FetchType.EAGER)
    private List<HddPriceList> PriceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hdd", fetch = FetchType.LAZY)
    private List<HddRating> hddRatingList;

    public List<HddPriceList> getPriceList() {
        return PriceList;
    }

    public void setPriceList(List<HddPriceList> PriceList) {
        this.PriceList = PriceList;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String size) {
        this.storage = size;
    }

    @Override
    public Double getAverageRating() {

        if (hddRatingList.isEmpty()) {
            return null;
        } else {
            double avg = 0.0;
            for (HddRating obj : this.hddRatingList) {
                avg += obj.getRating();
            }
            avg = avg / this.hddRatingList.size();
            return Utility.to2DecimalDouble(avg);
        }

    }

    public Optional<HddRating> getHddRating() {
        return hddRating;
    }

    public void setHddRating(Optional<HddRating> hddRating) {
        this.hddRating = hddRating;
    }

    @Override
    public Integer getNumberOfRating() {
        return this.hddRatingList.size();
    }

    public int getMinPrice(){
        int min = 500000000;
        for(HddPriceList hddPriceList : this.PriceList)
        {
            if(hddPriceList.getPrice() < min) {
                min = hddPriceList.getPrice();
            }
        }
        return min;
    }
}
