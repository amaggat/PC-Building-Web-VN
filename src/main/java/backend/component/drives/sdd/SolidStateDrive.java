package backend.component.drives.sdd;


import backend.component.common.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.SsdRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "ssd")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SolidStateDrive extends ElectronicComponents {

    @Column(name = "storage")
    @NotEmpty
    private String storage;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ssd")
    private List<PcProfile> pcProfile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ssd", fetch = FetchType.EAGER)
    private List<SsdPriceList> PriceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ssd", fetch = FetchType.LAZY)
    private List<SsdRating> ssdRatingList;

    @Transient
    private Optional<SsdRating> ssdRating;
}
