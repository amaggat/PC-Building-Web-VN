package backend.component.gpu.entity;

import backend.component.common.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.GpuRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "gpu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphicProcessor extends ElectronicComponents {

    @Column(name = "VRam")
    @NotEmpty
    @Digits(fraction = 0, integer = 2)
    private Integer VRam;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "graphicProcessor", fetch = FetchType.EAGER)
    private List<GpuComponentPrice> PriceList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "graphicProcessor")
    private List<PcProfile> pcProfileList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "graphicProcessor", fetch = FetchType.LAZY)
    private List<GpuRating> gpuRatingList;

    @Transient
    Optional<GpuRating> gpuRating;

}
