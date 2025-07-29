package com.app.library.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanDeadlineInfo {
    private Long days;
    private boolean isOverdue;
}
