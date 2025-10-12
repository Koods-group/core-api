package ci.koodysgroup.interfaces.bus;

import ci.koodysgroup.interfaces.query.Query;
import ci.koodysgroup.interfaces.handlers.QueryHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QueryBus {

    private final ApplicationContext context;
    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();

    public QueryBus(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {
        Map<String, QueryHandler> handlerBeans = context.getBeansOfType(QueryHandler.class);
        handlerBeans.forEach((name, handler) -> {
            Class<?> queryType = getQueryType(handler);
            handlers.put(queryType, handler);
        });
    }

    private Class<?> getQueryType(QueryHandler<?, ?> handler) {
        ResolvableType resolvableType = ResolvableType.forClass(handler.getClass())
                .as(QueryHandler.class);
        return resolvableType.getGeneric(0).resolve();
    }

    @SuppressWarnings("unchecked")
    public <Q extends Query<R>, R> R send(Q query) {
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) handlers.get(query.getClass());
        if (handler != null) {
            return handler.handler(query);
        } else {
            throw new RuntimeException("No handler found for query: " + query.getClass().getSimpleName());
        }
    }

}
