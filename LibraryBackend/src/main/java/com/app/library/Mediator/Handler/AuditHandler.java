package com.app.library.Mediator.Handler;

import com.app.library.DTO.Request.AuditRequest;
import com.app.library.Service.Interfaces.AuditService;
import org.springframework.stereotype.Component;

@Component
public class AuditHandler implements IRequestHandler<AuditRequest> {
    private final AuditService auditService;
    public AuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void handle(AuditRequest auditRequest) {
        if(auditRequest == null) {throw new IllegalArgumentException("AuditRequest is null");}
        auditService.log(auditRequest);
    }
}
