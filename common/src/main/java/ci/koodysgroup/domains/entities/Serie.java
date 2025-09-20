package ci.koodysgroup.domains.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "series")
public class Serie extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @Column(name = "seasons")
    private int seasons;

    @Column(name = "episodes")
    private int episodes;

    @Column(name = "showrunner")
    private String showrunner;
}
