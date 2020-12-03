package ru.otus.gc;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/*
О формате логов
http://openjdk.java.net/jeps/158


-Xms512m -Xmx512m -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -XX:+UseG1GC
*/

/*
1)
    default, time: 83 sec (82 without Label_1)
2)
    -XX:MaxGCPauseMillis=100000, time: 82 sec //Sets a target for the maximum GC pause time.
3)
    -XX:MaxGCPauseMillis=10, time: 91 sec

4)
-Xms2048m
-Xmx2048m
    time: 81 sec

5)
-Xms5120m
-Xmx5120m
    time: 80 sec

5)
-Xms20480m
-Xmx20480m
    time: 81 sec (72 without Label_1)

*/

public class GcDemo {
    private static final Map<GarbageCollectorMXBean, ComputeInfo> map = new ConcurrentHashMap<>();

    public static void main(String... args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();
        long beginTime = System.currentTimeMillis();
        int loopCounter = 1000;
        Benchmark mbean = new Benchmark(loopCounter, 5_000_000, false);
        try {
            mbean.run();
        } catch (OutOfMemoryError e) {
            System.out.println("OOOPS OOM");
        }

        System.out.println("time:" + (System.currentTimeMillis() - beginTime));
        System.out.println(map.entrySet().stream().map(entry -> entry.getKey().getName() + " : " + entry.getValue().toString()).collect(Collectors.joining("\n")));
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            map.put(gcbean, new ComputeInfo(Integer.MAX_VALUE, 0, 0, 0));
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    long duration = info.getGcInfo().getDuration();
                    map.computeIfPresent(gcbean, (bean, computeInfo) -> computeInfo.add(duration));
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    private static final class ComputeInfo {
        private final long min;
        private final long max;
        private final long count;
        private final long duration;

        public ComputeInfo(long min, long max, long count, long duration) {
            this.min = min;
            this.max = max;
            this.count = count;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "ComputeInfo{" +
                    "min=" + min +
                    ", max=" + max +
                    ", count=" + count +
                    ", duration=" + duration +
                    '}';
        }

        ComputeInfo add(long duration) {
            return new ComputeInfo(Math.min(duration, min), Math.max(max, duration), count + 1, this.duration + duration);
        }

    }
}
