package backend.component.psu;


import backend.retailer.Retailer;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "psu_price_list")
public class PsuPriceList {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    private PowerSupplyUnit psu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rid")
    private Retailer retailer;

    @Column(name = "link")
    @NotEmpty
    private String link = new String();

    @Column(name = "price")
    @NotEmpty
    private int price;

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(String Retailer) {
        this.retailer = retailer;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
