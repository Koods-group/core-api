package ci.koodysgroup.domains.dtms;

import ci.koodysgroup.domains.entities.Otp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class OtpDtm {


    @JsonProperty("id")
    private UUID id;

    @JsonProperty("counter")
    private int counter;

    @JsonProperty("consumed")
    private boolean consumed;

    @JsonProperty("generated_by")
    private String generatedBy;


    public static OtpDtm fromCodeDtm(Otp otp)
    {
        return new OtpDtm(
                otp.getId(),
                otp.getCounter(),
                otp.isConsumed(),
                otp.getGeneratedBy()
        );
    }
}
