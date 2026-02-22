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

        // heap 멤모리 사용량 추출
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryMXBean.getHeapMemoryUsage();

        long metaUsed = 0L;
        long metaCommitted = 0L;
        // 메타스페이스 사용량 추출
        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean pool : pools) {
            if ("Metaspace".equals(pool.getName())) {
                MemoryUsage memoryUsage = pool.getUsage();
                metaUsed = memoryUsage.getUsed();
                metaCommitted = memoryUsage.getCommitted();
                break;
            }
        }

        // 스레스 목록/상태 추출
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadMXBean.getAllThreadIds();
        ThreadInfo[] infos = threadMXBean.getThreadInfo(threadIds);

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
