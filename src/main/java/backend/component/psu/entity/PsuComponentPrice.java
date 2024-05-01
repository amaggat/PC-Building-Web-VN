package backend.component.psu.entity;


import backend.component.common.model.ComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "psu_price_list")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsuComponentPrice extends ComponentPrice {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    private PowerSupplyUnit psu;
}
