package backend.model;

import backend.recommendation.type.score.Category;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@MappedSuperclass
public class ElectronicComponents {

    @Id
    private String id = new String();

    @Column(name = "manufacturer")
    private String manufacturer = new String();

    @Column(name = "seriename")
    @NotEmpty
    private String serieName = new String();

    @Column(name = "chipset")
    private String chipset = new String();

    @Column(name= "image")
    @NotEmpty
    private String image = new String();

    @Column(name = "fullname")
    @NotEmpty
    private String fullname = new String();

    @ManyToOne
    @JoinColumn(name= "categoryid")
    private Category category;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
