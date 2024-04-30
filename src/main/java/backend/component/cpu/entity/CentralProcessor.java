package backend.component.cpu.entity;


import backend.component.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.CpuRating;
import backend.utility.Utility;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "cpu")
public class CentralProcessor extends ElectronicComponents {

    @Column(name = "socket")
    @NotEmpty
    private String socket = new String();

    @Column(name = "cores")
    @NotEmpty
    private Integer cores;

    @Column(name = "threads")
    @NotEmpty
    private Integer Threads;

    @Transient
    private int minPrice;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "centralProcessor", fetch = FetchType.EAGER)
    private List<CpuPriceList> PriceList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "centralProcessor")
    private List<PcProfile> pcProfileList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "centralProcessor")
    private List<CpuRating> cpuRatingList;

    @Transient
    private Optional<CpuRating> cpuRating;

    @Override
    public Double getAverageRating() {

        if (cpuRatingList.isEmpty()) {
            return null;
        } else {
            double avg = 0.0;
            for (CpuRating obj : this.cpuRatingList) {
                avg += obj.getRating();
            }
            avg = avg / this.cpuRatingList.size();
            return Utility.to2DecimalDouble(avg);
        }
    }

    @Override
    public Integer getNumberOfRating() {
        return this.cpuRatingList.size();
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public Integer getCores() {
        return cores;
    }

    public void setCores(Integer cores) {
        this.cores = cores;
    }

    public Integer getThreads() {
        return Threads;
    }

    public void setThreads(Integer threads) {
        Threads = threads;
    }

    public List<CpuPriceList> getPriceList() {
        return PriceList;
    }

    public void setPriceList(List<CpuPriceList> cpuPriceList) {
        this.PriceList = cpuPriceList;
    }

    public List<PcProfile> getPcProfileList() {
        return this.pcProfileList;
    }

    public void setPcProfileList(List<PcProfile> pcProfileList) {
        this.pcProfileList = pcProfileList;
    }

    public void setCpuRatingList(List<CpuRating> cpuRatingList) {
        this.cpuRatingList = cpuRatingList;
    }

    public Optional<CpuRating> getCpuRating() {
        return cpuRating;
    }

    public void setCpuRating(Optional<CpuRating> cpuRating) {
        this.cpuRating = cpuRating;
    }

    public int getMinPrice(){
        int min = 500000000;
        for(CpuPriceList cpuPriceList : this.PriceList)
        {
            if(cpuPriceList.getPrice() < min) {
                min = cpuPriceList.getPrice();
            }
        }
        return min;
    }
}
