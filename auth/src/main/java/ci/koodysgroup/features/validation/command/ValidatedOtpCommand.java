package ci.koodysgroup.features.validation.command;

import ci.koodysgroup.interfaces.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidatedOtpCommand implements Command<String> {

    private UUID otpId;
    private String code;
    private String generatedBy;
}
