package com.app.library.Mediator;

import com.app.library.DTO.Request.AuditRequest;
import org.springframework.stereotype.Component;

@Component
public interface IMediator {
    <T> void send(T request);
}
