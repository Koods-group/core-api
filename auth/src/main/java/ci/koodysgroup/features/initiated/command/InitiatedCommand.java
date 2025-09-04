package ci.koodysgroup.features.initiated.command;

import ci.koodysgroup.interfaces.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InitiatedCommand implements Command<String> {

    private UUID country_id;
    private String generated_by;

}
