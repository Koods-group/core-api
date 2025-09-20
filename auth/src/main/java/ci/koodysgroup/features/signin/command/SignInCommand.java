package ci.koodysgroup.features.signin.command;


import ci.koodysgroup.interfaces.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInCommand implements Command<String> {

    private String login;
    private String password;

}
