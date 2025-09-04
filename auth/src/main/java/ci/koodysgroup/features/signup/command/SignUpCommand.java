package ci.koodysgroup.features.signup.command;


import ci.koodysgroup.interfaces.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpCommand implements Command<String> {

    private UUID otp_id;
    private String civility;
    private String name;
    private String surname;
    private String login;
}
