package com.app.library.DTO.Request;

import java.time.LocalDateTime;

public class AuditRequest {
    private String action;
    private String entity;
    private String performedBy;
    private LocalDateTime timestamp;
    private String details;
    private Object object;

    public AuditRequest(String action, String entity, String performedBy, LocalDateTime timestamp, String details, Object object) {
        this.action = action;
        this.entity = entity;
        this.performedBy = performedBy;
        this.timestamp = timestamp;
        this.details = details;
        this.object = object;
    }

    public AuditRequest() {

    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
