package backend.component.ram;


import backend.component.common.model.ComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ram_price_list")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RamComponentPrice extends ComponentPrice {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    private Ram ram;

}
