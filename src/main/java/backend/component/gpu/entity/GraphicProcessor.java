package backend.component.gpu.entity;

import backend.component.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.GpuRating;
import backend.utility.Utility;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "gpu")
public class GraphicProcessor extends ElectronicComponents {

    @Column(name = "VRam")
    @NotEmpty
    @Digits(fraction = 0, integer = 2)
    private Integer VRam;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "graphicProcessor", fetch = FetchType.EAGER)
    private List<GpuPriceList> PriceList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "graphicProcessor")
    private List<PcProfile> pcProfileList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "graphicProcessor", fetch = FetchType.LAZY)
    private List<GpuRating> gpuRatingList;

    @Transient
    Optional<GpuRating> gpuRating;

//    public List<GpuRating> getGpuRatingList() {
//        return gpuRatingList;
//    }

    public void setGpuRatingList(List<GpuRating> gpuRatingList) {
        this.gpuRatingList = gpuRatingList;
    }

    public List<GpuPriceList> getPriceList() {
        return PriceList;
    }

    public void setPriceList(List<GpuPriceList> gpuPriceList) {
        this.PriceList = gpuPriceList;
    }

    public Integer getVRam() {
        return VRam;
    }

    public void setVRam(Integer VRam) {
        this.VRam = VRam;
    }

    public List<String> getPcProfileList() {
        return Utility.returnPcProfileID(this.pcProfileList);
    }

    public void setPcProfileList(List<PcProfile> pcProfile) {
        this.pcProfileList = pcProfile;
    }

    public Optional<GpuRating> getGpuRating() {
        return gpuRating;
    }

    public void setGpuRating(Optional<GpuRating> gpuRating) {
        this.gpuRating = gpuRating;
    }

    @Override
    public Double getAverageRating() {

        if (gpuRatingList.isEmpty()) {
            return null;
        } else {
            double avg = 0.0;
            for (GpuRating obj : this.gpuRatingList) {
                avg += obj.getRating();
            }
            avg = avg / this.gpuRatingList.size();
            return Utility.to2DecimalDouble(avg);
        }

    }

    @Override
    public Integer getNumberOfRating() {
        return this.gpuRatingList.size();
    }

    public GraphicProcessor() {
    }

    @Transient
    private int minPrice;

    public int getMinPrice(){
        int min = 500000000;
        for(GpuPriceList gpuPriceList : this.PriceList)
        {
            if(gpuPriceList.getPrice() < min) {
                min = gpuPriceList.getPrice();
            }
        }
        return min;
    }
}
