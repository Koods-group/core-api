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

    @Column(name = "libelle" , unique = true)
    private String libelle;

    @Column(name = "alias" , unique = true)
    private String alias;

    @Column(name = "flag", unique = true)
    private String flag;

    @Column(name = "number_length")
    private int numberLength;

    @Column(name = "country_code" , unique = true)
    private String countryCode ;

    public Country(String libelle , String alias, String flag, String countryCode, int numberLength)
    {
        this.libelle = libelle;
        this.alias = alias;
        this.flag = flag;
        this.countryCode = countryCode;
        this.numberLength = numberLength;
    }
}
