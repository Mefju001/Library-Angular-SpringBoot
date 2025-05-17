package com.app.library.DTO.Request;

import java.time.LocalDateTime;

public record AuditRequest(String action, String entity, String performedBy, LocalDateTime timestamp,
                           String details, Object object) {

}
