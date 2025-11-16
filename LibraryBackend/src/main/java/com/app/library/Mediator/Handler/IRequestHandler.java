package com.app.library.Mediator.Handler;

public interface IRequestHandler<TRequest> {
    void handle(TRequest request);
}
