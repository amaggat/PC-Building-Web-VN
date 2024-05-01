package backend.component.cpu.entity;


import backend.component.common.model.ComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cpu_price_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpuComponentPrice extends ComponentPrice {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    private CentralProcessor centralProcessor;

}
