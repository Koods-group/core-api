package ci.koodysgroup.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "access" ,
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"country" , "login"})
    }
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Access extends AbstractDateTime implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "locked_account")
    private boolean locked_account = false;

    @Column(name = "connected")
    private String connected = "unconnected";

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_key")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getAlias()));
    }

    @OneToMany(mappedBy = "access" , cascade = CascadeType.ALL)
    private List<Token> tokens;

    public Access(String login, User user, String password, Role role, Country country)
    {
        this.login = login;
        this.user = user;
        this.password = password;
        this.role = role;
        this.country = country;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
