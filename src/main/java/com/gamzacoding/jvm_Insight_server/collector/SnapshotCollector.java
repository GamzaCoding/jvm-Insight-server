package com.gamzacoding.jvm_Insight_server.collector;

import com.gamzacoding.jvm_Insight_server.api.dto.Collected;
import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class SnapshotCollector {

    public static final String METASPACE_POOL_NAME = "Metaspace";

    public Collected collectFromCurrentJvm() {
        Instant now = Instant.now();
        HeapSnapshot heap = readHeap();
        PoolSnapshot metaspace = readMemoryPool(METASPACE_POOL_NAME).orElse(PoolSnapshot.ZERO);
        ThreadSnapshot threads = readThreadSnapshot();

        return new Collected(
                now,
                heap.used(), heap.committed(), heap.max(),
                metaspace.used(), metaspace.committed(),
                threads.threadCount(),
                threads.runnable(), threads.blocked(), threads.waiting()
        );
    }

    private HeapSnapshot readHeap() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryMXBean.getHeapMemoryUsage();
        return new HeapSnapshot(heap.getUsed(), heap.getCommitted(), heap.getMax());
    }

    private Optional<PoolSnapshot> readMemoryPool(String poolName) {
        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean pool : pools) {
            if (poolName.equals(pool.getName())) {
                MemoryUsage usage = pool.getUsage();
                if (usage == null) {
                    return Optional.empty();
                }
                return Optional.of(new PoolSnapshot(usage.getUsed(), usage.getCommitted()));
            }
        }
        return Optional.empty();
    }

    private ThreadSnapshot readThreadSnapshot() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] ids = threadMXBean.getAllThreadIds();
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(ids);

        int runnable = 0;
        int blocked = 0;
        int waiting = 0;

        for (ThreadInfo info : threadInfos) {
            if (info == null) {
                continue;
            }

            State threadState = info.getThreadState();
            if (threadState == State.RUNNABLE) {
                runnable++;
            } else if (threadState == State.BLOCKED) {
                blocked++;
            } else if (threadState == State.WAITING || threadState == State.TIMED_WAITING) {
                waiting++;
            }
        }
        return new ThreadSnapshot(threadMXBean.getThreadCount(), runnable, blocked, waiting);
    }

    private record HeapSnapshot(long used, long committed, long max) {}

    private record PoolSnapshot(long used, long committed) {
        static final PoolSnapshot ZERO = new PoolSnapshot(0L, 0L);
    }

    private record ThreadSnapshot(int threadCount, int runnable, int blocked, int waiting) {}
}
