package ci.koodysgroup.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "countries")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Country extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "alias")
    private String alias;

    @Column(name = "flag")
    private String flag;

    @Column(name = "number_length")
    private int number_length;

    @Column(name = "country_code")
    private String country_code ;
}
