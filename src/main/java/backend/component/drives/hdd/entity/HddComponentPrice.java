package backend.component.drives.hdd.entity;


import backend.component.common.model.ComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "hdd_price_list")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HddComponentPrice extends ComponentPrice {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    private HardDiskDrive hdd;

}
