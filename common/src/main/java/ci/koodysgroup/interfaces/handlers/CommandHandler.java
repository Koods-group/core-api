package ci.koodysgroup.interfaces.handlers;

import ci.koodysgroup.interfaces.command.Command;
import org.springframework.stereotype.Component;

@Component
public interface CommandHandler<C extends Command<?>> {
    void handler(C command);
}
