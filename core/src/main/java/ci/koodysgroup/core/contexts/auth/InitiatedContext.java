package ci.koodysgroup.core.contexts.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class InitiatedContext {

    private String generatedBy;
    private UUID countryId;
}
