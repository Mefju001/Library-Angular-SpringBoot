package com.app.library.Mediator;

import com.app.library.Mediator.Interfaces.ICommand;
import com.app.library.Mediator.Interfaces.ICommandHandler;
import com.app.library.Mediator.Interfaces.IQueryHandler;
import com.app.library.Mediator.Interfaces.IRequest;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Component
public class Mediator{
    Map<Class<?>, ICommandHandler<?>> handlers = new HashMap<>();
    Map<Class<?>,IQueryHandler<?,?>> queryHandlers = new HashMap<>();
    public Mediator(Collection<?> handlers) {
        for(Object handler: handlers){
            Class<?> clazz = getHandlerClass(handler);
            if(clazz!=null){
                if (handler instanceof IQueryHandler) {
                    this.queryHandlers.put(clazz, (IQueryHandler<?, ?>) handler);
                } else if (handler instanceof ICommandHandler) {
                    this.handlers.put(clazz, (ICommandHandler<?>) handler);
                }
            }
        }
    }
    private Class<?> getHandlerClass(Object handler){
        Type[]genericInterfaces = handler.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType)
            {
                Type rawType = parameterizedType.getRawType();
                if(rawType.equals(IQueryHandler.class)||rawType.equals(ICommandHandler.class)){
                    Type requestType = parameterizedType.getActualTypeArguments()[0];
                    if (requestType instanceof Class) {
                        return (Class<?>) requestType;
                    }
                }
            }
        }
        return null;
    }
    public void send(ICommand request) {
        Class<?> requestType = request.getClass();
        ICommandHandler<?> handler = handlers.get(requestType);

        if (handler == null) {

            throw new IllegalArgumentException("No handler registered for request type: " + requestType.getName());
        }

        try {
            @SuppressWarnings("unchecked")
            ICommandHandler<ICommand> castedHandler = (ICommandHandler<ICommand>) handler;

            castedHandler.handle(request);

        } catch (ClassCastException e) {
            throw new RuntimeException("Handler type mismatch for request: " + requestType.getName(), e);
        }
    }
    public <TResponse> TResponse send(IRequest<TResponse> request) {
        Class<?> requestType = request.getClass();
        IQueryHandler<?,?> queryHandler = queryHandlers.get(requestType);
        if (queryHandler == null) {
            throw new IllegalArgumentException("No handler registered for request type: " + requestType.getName());
        }
        try{
            @SuppressWarnings("unchecked")
            IQueryHandler<IRequest<TResponse>,TResponse> castedQueryHandler = (IQueryHandler<IRequest<TResponse>, TResponse>) queryHandler;
            return castedQueryHandler.handle(request);
        }catch (ClassCastException e){
            throw new RuntimeException("Handler type mismatch for request: " + requestType.getName(), e);
        }
    }
}
