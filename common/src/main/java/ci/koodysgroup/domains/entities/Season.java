package ci.koodysgroup.domains.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Seasons")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Season extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "serie_id" , nullable = false)
    private Serie serie;

    @Column(name = "season_number")
    private int seasonNumber;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "must_be_seen")
    private boolean mustBeSeen;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Episode> episodes = new ArrayList<>();
}
