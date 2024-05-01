package backend.component.mainboard.entity;


import backend.component.common.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.MainboardRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "mainboard")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mainboard extends ElectronicComponents {

    @Column(name = "socket")
    @NotEmpty
    private String socket = new String();

    @Column(name = "formfactor")
    @NotEmpty
    private String formFactor = new String();

    @Column(name = "sizeofram")
    @NotEmpty
    private String sizeOfRam = new String();

    @Column(name = "slot")
    @NotEmpty
    private Integer memorySlot;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mainboard", fetch = FetchType.EAGER)
    private List<MainboardComponentPrice> priceList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "main")
    private List<PcProfile> pcProfileList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mainboard", fetch = FetchType.LAZY)
    private List<MainboardRating> mainboardRatingList;

    @Transient
    private Optional<MainboardRating> mainboardRating;
}
