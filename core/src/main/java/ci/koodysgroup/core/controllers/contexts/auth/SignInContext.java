package ci.koodysgroup.core.controllers.contexts.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class SignInContext {

    private String login;
    private String password;
}
