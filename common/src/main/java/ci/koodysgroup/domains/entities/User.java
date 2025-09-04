package ci.koodysgroup.domains.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "civility")
    private String civility;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "number")
    private String number;

    @Column(name = "avatar" , nullable = true)
    private String avatar;

    @Column(name = "birthday", nullable = true)
    private String birthday;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private Access access;

    public User(String civility, String name, String surname, String number)
    {
        this.civility = civility;
        this.name = name;
        this.surname = surname;
        this.number = number;
    }
}
