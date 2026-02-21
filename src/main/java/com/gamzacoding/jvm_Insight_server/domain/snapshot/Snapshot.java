package com.gamzacoding.jvm_Insight_server.domain.snapshot;

import com.gamzacoding.jvm_Insight_server.domain.target.Target;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "snapshots")
public class Snapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Target target;

    @Column(name = "captured_at", nullable = false)
    private Instant capturedAt;

    @Column(name = "heap_used", nullable = false)
    private long heapUsed;

    @Column(name = "heap_committed", nullable = false)
    private long heapCommitted;

    @Column(name = "heap_max", nullable = false)
    private long heapMax;

    @Column(name = "meta_used", nullable = false)
    private long metaUsed;

    @Column(name = "meta_committed", nullable = false)
    private long metaCommitted;

    @Column(name = "thread_count", nullable = false)
    private int threadCount;

    @Column(name = "runnable_count", nullable = false)
    private int runnableCount;

    @Column(name = "blocked_count", nullable = false)
    private int blockedCount;

    @Column(name = "waiting_count", nullable = false)
    private int waitingCount;

    public static Snapshot of(Target target, Instant capturedAt,
                              long heapUsed, long heapCommitted, long heapMax,
                              long metaUsed, long metaCommitted,
                              int threadCount, int runnableCount, int blockedCount, int waitingCount) {

        Snapshot s = new Snapshot();
        s.target = target;
        s.capturedAt = capturedAt;
        s.heapUsed = heapUsed;
        s.heapCommitted = heapCommitted;
        s.heapMax = heapMax;
        s.metaUsed = metaUsed;
        s.metaCommitted = metaCommitted;
        s.threadCount = threadCount;
        s.runnableCount = runnableCount;
        s.blockedCount = blockedCount;
        s.waitingCount = waitingCount;
        return s;
    }
}
