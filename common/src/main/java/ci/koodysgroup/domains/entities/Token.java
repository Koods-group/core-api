package ci.koodysgroup.domains.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Token extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "access_key")
    private Access access;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public  Token(LocalDateTime expired , Access access , String token){
        this.expiredAt = expired;
        this.access = access;
        this.token = token;
    }


}
