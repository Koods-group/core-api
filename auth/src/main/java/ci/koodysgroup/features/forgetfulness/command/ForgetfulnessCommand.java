package ci.koodysgroup.features.forgetfulness.command;

import ci.koodysgroup.interfaces.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ForgetfulnessCommand implements Command<String> {
    private UUID countryId;
    private String login;
}
