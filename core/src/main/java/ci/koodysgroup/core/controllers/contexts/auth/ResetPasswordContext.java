package ci.koodysgroup.core.controllers.contexts.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class ResetPasswordContext {

    private String passorwd;
    private UUID countryId;
    private String login;
    private UUID otpId;

}
