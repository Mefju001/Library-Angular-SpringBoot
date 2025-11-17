package com.app.library.Service.Interfaces;

import com.app.library.DTO.MediatorRequest.AuditRequest;

public interface AuditService {
    void log(AuditRequest auditRequest);

    void logUpdate(String action, String entity, String user, Object oldObject, Object newObject);

    void writeToFile(AuditRequest event);
}
