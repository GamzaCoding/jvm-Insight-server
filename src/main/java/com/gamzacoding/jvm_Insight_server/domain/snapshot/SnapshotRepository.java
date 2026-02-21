package com.gamzacoding.jvm_Insight_server.domain.snapshot;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {
    List<Snapshot> findByTargetIdAndCapturedAtBetweenOrderByCapturedAtAsc(Long targetId, Instant from, Instant to);
}
