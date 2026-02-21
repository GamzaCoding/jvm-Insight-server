package com.gamzacoding.jvm_Insight_server.api;

import com.gamzacoding.jvm_Insight_server.controller.SnapshotController;
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
    private final SnapshotController controller = new SnapshotController();

    public Snapshot captureNow(Long targetId) {
        Target target = targetRepository.findById(targetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "타겟을 찾을 수 없습니다."));

        SnapshotController.Collected c = controller.collectFromCurrentJvm();

        Snapshot snapshot = Snapshot.of(
                target,
                c.capturedAt(),
                c.heapUsed(), c.heapCommitted(), c.heapMax(),
                c.metaUsed(), c.metaCommitted(),
                c.threadCount(), c.runnableCount(), c.blockedCount(), c.waitingCount()
        );

        return snapshotRepository.save(snapshot);
    }

    public List<Snapshot> findBetween(Long targetId, Instant from, Instant to) {
        Instant f = (from != null) ? from : Instant.now().minus(1, ChronoUnit.HOURS);
        Instant t = (to != null) ? to : Instant.now();

        return snapshotRepository.findByTargetIdAndCapturedAtBetweenOrderByCapturedAtAsc(targetId, f, t);
    }
}
