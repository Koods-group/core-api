package ci.koodysgroup.interfaces.handlers;

import ci.koodysgroup.interfaces.Query.Query;
import org.springframework.stereotype.Component;

@Component
public interface QueryHandler<Q extends Query<?>, R> {
    R handler(Q query);
}
