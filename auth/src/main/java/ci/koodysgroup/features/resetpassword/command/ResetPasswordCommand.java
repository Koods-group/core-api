package ci.koodysgroup.features.resetpassword.command;

import ci.koodysgroup.interfaces.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordCommand implements Command<String> {

    private String passorwd;
    private UUID countryId;
    private String login;
    private UUID otpId;
}
