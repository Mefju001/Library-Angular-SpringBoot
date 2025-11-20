package com.app.library.Mediator.Interfaces;


public interface ICommandHandler<TCommand extends ICommand> {
    void handle(TCommand command);
}
