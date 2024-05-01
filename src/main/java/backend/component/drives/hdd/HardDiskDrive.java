package backend.component.drives.hdd;

import backend.component.common.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.HddRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "hdd")
@AllArgsConstructor
@NoArgsConstructor
public class HardDiskDrive extends ElectronicComponents {

    @Column(name = "storage")
    @NotEmpty
    private String storage;

    @Transient
    Optional<HddRating> hddRating;
}
