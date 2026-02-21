package com.gamzacoding.jvm_Insight_server.domain.target;

import com.gamzacoding.jvm_Insight_server.domain.snapshot.Snapshot;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetRepository extends JpaRepository<Target, Long> {
    // 리팩터링 필요 부분
    List<Snapshot> findByTargetIdAndCapturedAtBetweenOrderByCapturedAtAsc(Long targetId, Instant to);
}
