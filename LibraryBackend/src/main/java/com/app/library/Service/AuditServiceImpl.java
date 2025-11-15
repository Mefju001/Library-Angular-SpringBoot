package com.app.library.Service;

import com.app.library.DTO.Request.AuditRequest;
import com.app.library.Service.Interfaces.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
    public void logUpdate(String action, String entity, String user, Object oldObject, Object newObject) {
        Map<String, Object> changes = findDifferences(oldObject, newObject);
        String details = "Zmodyfikowane pola: " + changes;

        AuditRequest audit = new AuditRequest("Update", entity, user, LocalDateTime.now(), details, newObject);
        writeToFile(audit);
    }

    private Map<String, Object> findDifferences(Object oldObject, Object newObject) {
        Map<String, Object> differences = new HashMap<>();
        try {
            Class<?> clazz = oldObject.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object oldValue = field.get(oldObject);
                Object newValue = field.get(newObject);
                if (!Objects.equals(oldValue, newValue)) {
                    differences.put(field.getName(), "Zmieniono z '" + oldValue + "' na '" + newValue + "'");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return differences;
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
