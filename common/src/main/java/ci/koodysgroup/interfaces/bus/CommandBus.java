package ci.koodysgroup.interfaces.bus;

import ci.koodysgroup.interfaces.command.Command;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommandBus {
    private final ApplicationContext context ;
    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new HashMap<>();

    public CommandBus(ApplicationContext context)
    {
        this.context = context;
    }

    @PostConstruct
    public void init() {
        Map<String, CommandHandler> handlerBeans = context.getBeansOfType(CommandHandler.class);
        handlerBeans.forEach((name, handler) -> {
            Class<?> commandType = getCommandType(handler);
            handlers.put(commandType, handler);
        });
    }

    private Class<?> getCommandType(CommandHandler<?, ?> handler) {
        ResolvableType resolvableType = ResolvableType.forClass(handler.getClass())
                .as(CommandHandler.class);
        return resolvableType.getGeneric(0).resolve();
    }


    @SuppressWarnings("unchecked")
    public <C extends Command<?>, R> R send(C command) {
        CommandHandler<C, R> handler = (CommandHandler<C, R>) handlers.get(command.getClass());
        if (handler != null) {
            return handler.handler(command);
        } else {
            throw new RuntimeException("No handler found for command: " + command.getClass().getSimpleName());
        }
    }
}
