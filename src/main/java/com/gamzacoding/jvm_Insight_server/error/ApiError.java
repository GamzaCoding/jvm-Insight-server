package com.gamzacoding.jvm_Insight_server.error;

import java.time.Instant;
import java.util.List;

public record ApiError(
        String code,
        String message,
        List<FieldViolation> fieldViolations,
        String path,
        Instant timeStamp
) {
    public record FieldViolation(String field, String message) {}
}
