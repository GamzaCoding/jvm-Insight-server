package com.gamzacoding.jvm_Insight_server.api;

import com.gamzacoding.jvm_Insight_server.domain.snapshot.Snapshot;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/targets/{targetId}/snapshots")
public class SnapshotController {

    private final SnapshotService snapshotService;

    @PostMapping
    public SnapshotResponse capture(@PathVariable Long targetId) {
        return SnapshotResponse.from(snapshotService.captureNow(targetId));
    }

    @GetMapping
    public List<SnapshotResponse> list(
            @PathVariable Long targetId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        return snapshotService.findBetween(targetId, from, to).stream().map(SnapshotResponse::from).toList();
    }

    public record SnapshotResponse(
            Long id,
            Instant capturedAt,
            long heapUsed, long heapCommitted, long heapMax,
            long metaUsed, long metaCommitted,
            int threadCount, int runnableCount, int blockedCount, int waitingCount
    ) {
        static SnapshotResponse from(Snapshot snapshot) {
            return new SnapshotResponse(
                    snapshot.getId(),
                    snapshot.getCapturedAt(),
                    snapshot.getHeapUsed(), snapshot.getHeapCommitted(), snapshot.getHeapMax(),
                    snapshot.getMetaUsed(), snapshot.getMetaCommitted(),
                    snapshot.getThreadCount(), snapshot.getRunnableCount(), snapshot.getBlockedCount(), snapshot.getWaitingCount()
            );
        }
    }
}
