package com.app.library.Service;

import com.app.library.DTO.MediatorRequest.AuditRequest;
import com.app.library.Service.Interfaces.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

@Service
public class AuditServiceImpl implements AuditService {
    private static final String FILE_PATH = "audit-log.json";

    @Override
    public void log(AuditRequest auditRequest) {
        if (Objects.isNull(auditRequest)) {throw new IllegalArgumentException("AuditRequest is null");}
        writeToFile(auditRequest);
    }

    @Override
    public void writeToFile(AuditRequest event) {
        try (FileWriter file = new FileWriter(FILE_PATH, true)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            file.write(mapper.writeValueAsString(event) + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace(); //logger
        }
    }
}
