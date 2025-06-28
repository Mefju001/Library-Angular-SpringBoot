package com.app.library.DTO.Response;

public record DashboardStatsResponse(
        Long userCount,
        Long loanCount,
        Long newBooksCount,
        Long overdueCount) {
}
