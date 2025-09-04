package ci.koodysgroup.core.contexts.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter

public class ValidatedContext {

    private UUID otpId;
    private String code;
    private String generatedBy;
}
