package backend.component.ram.entity;


import backend.component.common.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.RamRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "ram")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ram extends ElectronicComponents {

    @Column(name = "clockspeed")
    @NotEmpty
    private Integer clockSpeed;

    @Column(name = "sizeofram")
    @NotEmpty
    private String sizeOfRam = new String();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ram")
    private List<PcProfile> pcProfileList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ram", fetch = FetchType.EAGER)
    private List<RamComponentPrice> PriceList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ram")
    private List<RamRating> ramRatingList;

    @Transient
    private Optional<RamRating> ramRating;
}
