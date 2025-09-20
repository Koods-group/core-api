package ci.koodysgroup.domains.dtms;

import ci.koodysgroup.domains.entities.Access;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class ProfilDtm {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("civility")
    private String civility;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("avatar")
    private String avatar;

    public static ProfilDtm fromProfilDtm(Access access)
    {
        return new ProfilDtm(
                access.getId(),
                access.getUser().getCivility(),
                access.getUser().getName(),
                access.getUser().getSurname(),
                access.getUser().getAvatar()
        );
    }
}
