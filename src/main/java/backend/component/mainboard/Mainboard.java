package backend.component.mainboard;


import backend.component.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.MainboardRating;
import backend.utility.Utility;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "mainboard")
public class Mainboard extends ElectronicComponents {

    @Column(name = "socket")
    @NotEmpty
    private String socket = new String();

    @Column(name = "formfactor")
    @NotEmpty
    private String formfactor = new String();

    @Column(name = "sizeofram")
    @NotEmpty
    private String sizeofram = new String();

    @Column(name = "slot")
    @NotEmpty
    private Integer memory_slot;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mainboard", fetch = FetchType.EAGER)
    private List<MainPriceList> PriceList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "main")
    private List<PcProfile> pcProfileList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mainboard", fetch = FetchType.LAZY)
    private List<MainboardRating> mainboardRatingList;

    @Transient
    private Optional<MainboardRating> mainboardRating;

    public Optional<MainboardRating> getMainboardRating() {
        return mainboardRating;
    }

    public void setMainboardRating(Optional<MainboardRating> mainboardRating) {
        this.mainboardRating = mainboardRating;
    }

    public String getFormfactor() {
        return this.formfactor;
    }

    public void setFormfactor(String size) {
        this.formfactor = size;
    }

    public List<MainPriceList> getPriceList() {
        return PriceList;
    }

    public void setPriceList(List<MainPriceList> mainPriceList) {
        this.PriceList = mainPriceList;
    }

    public String getSizeofram() {
        return sizeofram;
    }

    public void setSizeofram(String sizeofram) {
        this.sizeofram = sizeofram;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public Integer getMemory_slot() {
        return memory_slot;
    }

    public void setMemory_slot(Integer memory_slot) {
        this.memory_slot = memory_slot;
    }

    public List<String> getPcProfileList() {
        return Utility.returnPcProfileID(this.pcProfileList);
    }

    public void setPcProfileList(List<PcProfile> pcProfileList) {
        this.pcProfileList = pcProfileList;
    }

//    public List<MainboardRating> getMainboardRatingList() {
//        return mainboardRatingList;
//    }

    public void setMainboardRatingList(List<MainboardRating> mainboardRatingList) {
        this.mainboardRatingList = mainboardRatingList;
    }

    @Override
    public Double getAverageRating() {

        if (mainboardRatingList.isEmpty()) {
            return null;
        } else {
            double avg = 0.0;
            for (MainboardRating obj : this.mainboardRatingList) {
                avg += obj.getRating();
            }
            avg = avg / this.mainboardRatingList.size();
            return Utility.to2DecimalDouble(avg);
        }

    }

    @Override
    public Integer getNumberOfRating() {
        return this.mainboardRatingList.size();
    }

    @Transient
    private int minPrice;

    public int getMinPrice(){
        int min = 500000000;
        for(MainPriceList mainPriceList : this.PriceList)
        {
            if(mainPriceList.getPrice() < min) {
                min = mainPriceList.getPrice();
            }
        }
        return min;
    }
}
