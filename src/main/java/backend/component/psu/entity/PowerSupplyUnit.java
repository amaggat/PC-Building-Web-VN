package backend.component.psu.entity;

import backend.component.common.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.PsuRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "psu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerSupplyUnit extends ElectronicComponents {

    @Column(name = "power")
    @NotEmpty
    private Integer power;

    @Column(name = "standard_80")
    @NotEmpty
    private String standard_80 = new String();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "psu", fetch = FetchType.EAGER)
    private List<PsuComponentPrice> priceList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "psu")
    private List<PcProfile> pcProfileList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "psu")
    private List<PsuRating> psuRatingList;

    @Transient
    Optional<PsuRating> psuRating;
}
