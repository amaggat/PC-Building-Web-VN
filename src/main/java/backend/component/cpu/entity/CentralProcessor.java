package backend.component.cpu.entity;


import backend.component.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.CpuRating;
import backend.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "cpu")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CentralProcessor extends ElectronicComponents {

    @Column(name = "socket")
    @NotEmpty
    private String socket = new String();

    @Column(name = "cores")
    @NotEmpty
    private Integer cores;

    @Column(name = "threads")
    @NotEmpty
    private Integer Threads;

    @Transient
    private int minPrice;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "centralProcessor", fetch = FetchType.EAGER)
    private List<CpuPriceList> PriceList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "centralProcessor")
    private List<PcProfile> pcProfileList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "centralProcessor")
    private List<CpuRating> cpuRatingList;

    @Transient
    private Optional<CpuRating> cpuRating;

}
