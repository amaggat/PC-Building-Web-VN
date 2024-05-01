package backend.component.drives.hdd.entity;

import backend.component.common.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.HddRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "hdd")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HardDiskDrive extends ElectronicComponents {

    @Column(name = "storage")
    @NotEmpty
    private String storage;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "hdd")
    private List<PcProfile> pcProfile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hdd", fetch = FetchType.EAGER)
    private List<HddComponentPrice> priceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hdd", fetch = FetchType.LAZY)
    private List<HddRating> hddRatingList;

    @Transient
    Optional<HddRating> hddRating;
}
