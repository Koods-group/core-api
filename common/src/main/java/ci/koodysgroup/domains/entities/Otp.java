package ci.koodysgroup.domains.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "otps")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Otp extends AbstractDateTime{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(name = "code")
    private String code;


    @Column(name = "generated_by")
    private String generatedBy;


    @Column(name = "consumed")
    private boolean consumed = false;


    @Column(name = "counter")
    private int counter = 1;

    public Otp(String code, String generatedBy)
    {
        this.generatedBy = generatedBy;
        this.code = code ;
    }


}
