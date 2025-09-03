package ci.koodysgroup.interfaces.handlers;

import ci.koodysgroup.interfaces.Query.Query;
import org.springframework.stereotype.Component;

@Component
public interface QueryHandler<Q extends Query<?>, R> {
    R handler(Q query);

    default Class<Q> getQueryType() {
        return (Class<Q>) java.lang.reflect.ParameterizedType.class
                .cast(getClass().getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
    }
}
