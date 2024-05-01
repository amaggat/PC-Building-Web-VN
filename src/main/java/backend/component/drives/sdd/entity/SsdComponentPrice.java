package backend.component.drives.sdd.entity;


import backend.component.common.model.ComponentPrice;
import backend.retailer.Retailer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "ssd_price_list")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SsdComponentPrice extends ComponentPrice {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    private SolidStateDrive ssd;
}
