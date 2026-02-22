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
import org.springframework.stereotype.Component;

@Component
public class SnapshotCollector {

    public Collected collectFromCurrentJvm() {
        Instant now = Instant.now();

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryMXBean.getHeapMemoryUsage();

        long metaUsed = 0L;
        long metaCommitted = 0L;
        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean pool : pools) {
            if ("Metaspace".equals(pool.getName())) {
                MemoryUsage u = pool.getUsage();
                metaUsed = u.getUsed();
                metaCommitted = u.getCommitted();
                break;
            }
        }

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] ids = threadMXBean.getAllThreadIds();
        ThreadInfo[] infos = threadMXBean.getThreadInfo(ids);

        int runnable = 0;
        int blocked = 0;
        int waiting = 0;
        for (ThreadInfo info : infos) {
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

        return new Collected(
                now,
                heap.getUsed(), heap.getCommitted(), heap.getMax(),
                metaUsed, metaCommitted,
                threadMXBean.getThreadCount(),
                runnable, blocked, waiting
        );
    }
}
