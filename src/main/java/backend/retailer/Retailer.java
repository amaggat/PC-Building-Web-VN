package backend.retailer;

import backend.common.NameEntity;
import backend.component.cpu.entity.CpuPriceList;
import backend.component.drives.hdd.HddPriceList;
import backend.component.drives.sdd.SsdPriceList;
import backend.component.gpu.entity.GpuPriceList;
import backend.component.mainboard.MainPriceList;
import backend.component.psu.PsuPriceList;
import backend.component.ram.RamPriceList;
import backend.recommendation.rating.RetailerRating;
import backend.utility.Utility;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "retailer")
public class Retailer extends NameEntity {

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "shop")
    private String shop;

    @Column(name = "description")
    private String description;

    @Column(name = "logo")
    private String logo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "retailer")
    private List<RetailerRating> retailerRatingList;

    @Transient
    private Optional<RetailerRating> retailerRating;

    @Transient
    private Double avgRetailerRating;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "retailer")
    private List<CpuPriceList> cpuList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "retailer")
    private List<GpuPriceList> gpuList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "retailer")
    private List<MainPriceList> mainboardList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "retailer")
    private List<SsdPriceList> ssdList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "retailer")
    private List<HddPriceList> hddList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "retailer")
    private List<PsuPriceList> psuList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "retailer")
    private List<RamPriceList> ramList;

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public List<RetailerRating> getRetailerRatingList() {
//        return retailerRatingList;
//    }

    public void setRetailerRatingList(List<RetailerRating> retailerRatingList) {
        this.retailerRatingList = retailerRatingList;
    }

    public void setAvgRetailerRating(Double avgRetailerRating) {
        this.avgRetailerRating = avgRetailerRating;
    }

    public Double getAvgRetailerRating() {
        if (retailerRatingList.isEmpty()) {
            return null;
        } else {
            double avg = 0.0;
            for (RetailerRating obj : this.retailerRatingList) {
                avg += obj.getRating();
            }
            avg = avg / this.retailerRatingList.size();
            return Utility.to2DecimalDouble(avg);
        }
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Optional<RetailerRating> getRetailerRating() {
        return retailerRating;
    }

    public void setRetailerRating(Optional<RetailerRating> retailerRating) {
        this.retailerRating = retailerRating;
    }
}
