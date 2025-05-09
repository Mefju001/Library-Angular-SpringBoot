package com.app.library.Service;

import com.app.library.DTO.Request.AuditRequest;

import java.time.LocalDateTime;

public interface AuditService {
    void log(String action, String entity, String user, String details,Object object);
    void logUpdate(String action, String entity, String user, Object oldObject, Object newObject);
    void writeToFile(AuditRequest event);
}
