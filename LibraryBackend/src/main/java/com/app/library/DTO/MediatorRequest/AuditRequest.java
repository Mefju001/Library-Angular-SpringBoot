package com.app.library.DTO.MediatorRequest;

import com.app.library.Mediator.Interfaces.ICommand;

import java.time.LocalDateTime;

public record AuditRequest(String action, String entity, String performedBy, LocalDateTime timestamp,
                           String details, Object object) implements ICommand {

}
