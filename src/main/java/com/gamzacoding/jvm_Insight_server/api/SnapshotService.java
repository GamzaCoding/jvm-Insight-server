package com.gamzacoding.jvm_Insight_server.api;

import com.gamzacoding.jvm_Insight_server.api.dto.Collected;
import com.gamzacoding.jvm_Insight_server.collector.SnapshotCollector;
import com.gamzacoding.jvm_Insight_server.domain.snapshot.Snapshot;
import com.gamzacoding.jvm_Insight_server.domain.snapshot.SnapshotRepository;
import com.gamzacoding.jvm_Insight_server.domain.target.Target;
import com.gamzacoding.jvm_Insight_server.domain.target.TargetRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class SnapshotService {

    private final TargetRepository targetRepository;
    private final SnapshotRepository snapshotRepository;
    private final SnapshotCollector collector;

    public Snapshot captureNow(Long targetId) {
        Target target = targetRepository.findById(targetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "타겟을 찾을 수 없습니다."));

        Collected jvmCollected = collector.collectFromCurrentJvm();

        Snapshot snapshot = Snapshot.of(
                target,
                jvmCollected.capturedAt(),
                jvmCollected.heapUsed(), jvmCollected.heapCommitted(), jvmCollected.heapMax(),
                jvmCollected.metaUsed(), jvmCollected.metaCommitted(),
                jvmCollected.threadCount(), jvmCollected.runnableCount(), jvmCollected.blockedCount(), jvmCollected.waitingCount()
        );

        return snapshotRepository.save(snapshot);
    }

    public List<Snapshot> findBetween(Long targetId, Instant from, Instant to) {
        Instant f = (from != null) ? from : Instant.now().minus(1, ChronoUnit.HOURS);
        Instant t = (to != null) ? to : Instant.now();

        return snapshotRepository.findByTargetIdAndCapturedAtBetweenOrderByCapturedAtAsc(targetId, f, t);
    }
}
