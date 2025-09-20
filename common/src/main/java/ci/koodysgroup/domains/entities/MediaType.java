package ci.koodysgroup.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "media_types")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MediaType extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "mediaType", cascade = CascadeType.ALL)
    private List<Content> contents = new ArrayList<>();

}
