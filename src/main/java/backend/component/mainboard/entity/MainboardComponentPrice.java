package backend.component.mainboard.entity;


import backend.component.common.model.ComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mainboard_price_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainboardComponentPrice extends ComponentPrice {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    private Mainboard mainboard;

}
