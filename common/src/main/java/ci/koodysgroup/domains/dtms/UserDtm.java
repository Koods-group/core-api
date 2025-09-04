package ci.koodysgroup.domains.dtms;


import ci.koodysgroup.domains.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class UserDtm {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("civility")
    private String civility;

    @JsonProperty("avatar")
    private String avatar;


    public static UserDtm fromUserDtm(User user)
    {
        return new UserDtm(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getCivility(),
                user.getAvatar()
        );
    }
}
