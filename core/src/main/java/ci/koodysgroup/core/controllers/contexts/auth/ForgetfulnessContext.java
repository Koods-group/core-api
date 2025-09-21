package ci.koodysgroup.core.controllers.contexts.auth;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ForgetfulnessContext {
    private UUID countryId;
    private String login;
}
