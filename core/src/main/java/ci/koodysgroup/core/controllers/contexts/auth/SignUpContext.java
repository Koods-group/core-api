package ci.koodysgroup.core.controllers.contexts.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class SignUpContext {
    private UUID otpId;
    private UUID countryId;
    private String civility;
    private String name;
    private String surname;
    private String login;
    private String password;
}
