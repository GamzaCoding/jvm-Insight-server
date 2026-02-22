package com.gamzacoding.jvm_Insight_server.api.dto;

import java.time.Instant;

public record Collected(
        Instant capturedAt,
        long heapUsed, long heapCommitted, long heapMax,
        long metaUsed, long metaCommitted,
        int threadCount, int runnableCount, int blockedCount, int waitingCount) {
}
