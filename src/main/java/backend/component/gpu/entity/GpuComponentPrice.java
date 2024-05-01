package backend.component.gpu.entity;


import backend.component.common.model.ComponentPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "gpu_price_list")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpuComponentPrice extends ComponentPrice {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    private GraphicProcessor graphicProcessor;

}
