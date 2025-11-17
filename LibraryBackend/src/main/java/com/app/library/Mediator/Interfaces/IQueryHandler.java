package com.app.library.Mediator.Interfaces;


public interface IQueryHandler<TQuery extends IRequest<TResponse>, TResponse> {
    TResponse handle(TQuery query);
}
