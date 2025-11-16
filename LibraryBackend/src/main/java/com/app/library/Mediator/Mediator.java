package com.app.library.Mediator;

import com.app.library.Mediator.Handler.IRequestHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Component
public class Mediator implements IMediator {
    Map<Class<?>, IRequestHandler<?>> handlers = new HashMap<>();
    public Mediator(Collection<IRequestHandler<?>> handlers) {
        for(IRequestHandler<?> handler: handlers){
            Class<?> clazz = getHandlerClass(handler);
            if(clazz!=null){
                this.handlers.put(clazz, handler);
            }
        }
    }
    private Class<?> getHandlerClass(IRequestHandler<?> handler){
        Type[]genericInterfaces = handler.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType &&
                ((ParameterizedType) genericInterface).getRawType().equals(IRequestHandler.class)) {
                Type requestType = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
                return (Class<?>) requestType;
            }
        }
        return null;
    }
    @Override
    public <T> void send(T request) {
        Class<?> requestType = request.getClass();
        IRequestHandler<?> handler = handlers.get(requestType);

        if (handler == null) {

            throw new IllegalArgumentException("No handler registered for request type: " + requestType.getName());
        }

        try {
            @SuppressWarnings("unchecked")
            IRequestHandler<T> castedHandler = (IRequestHandler<T>) handler;

            castedHandler.handle(request);

        } catch (ClassCastException e) {
            throw new RuntimeException("Handler type mismatch for request: " + requestType.getName(), e);
        }
    }
}
