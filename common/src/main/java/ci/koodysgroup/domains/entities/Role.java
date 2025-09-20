package ci.koodysgroup.domains.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AbstractDateTime {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "libelle" , unique = true)
    private String libelle;

    @Column(name = "alias" , unique = true)
    private String alias;

    @OneToMany(mappedBy = "role" , cascade = CascadeType.ALL)
    private List<Access> access;


    public Role(String libelle, String alias)
    {
        this.libelle = libelle;
        this.alias = alias;
    }

}
