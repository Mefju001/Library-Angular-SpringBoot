package com.app.library.Entity;

public record LoanDeadlineInfo (
    Long days,
    boolean isOverdue
){}
