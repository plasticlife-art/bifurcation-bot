package com.math.bifurcation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;

import java.lang.management.ManagementFactory;

/**
 * @author Leonid Cheremshantsev
 */
public class MemoryHealthIndicator extends HealthIndicator {

    public static final int TRESHOLD = 10 * 1000 * 1000;
    private final Logger log = LoggerFactory.getLogger(MemoryHealthIndicator.class);

    public MemoryHealthIndicator() {
        super(null, null);
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        long freeMemory = getFreePhysicalMemorySize();
        long totalMemory = getTotalPhysicalMemorySize();
        long allocatedMemory = totalMemory - freeMemory;

        if (freeMemory > TRESHOLD) {
            builder.up();
        } else {
            log.warn(String.format("Free disk space below threshold. Available: %s (Treshold: %s)",
                    toHuman(freeMemory),
                    toHuman(TRESHOLD)));
            builder.status("WARN");
        }

        builder.withDetail("total", toHuman(totalMemory))
                .withDetail("used", toHuman(allocatedMemory))
                .withDetail("free", toHuman(freeMemory));
    }

    private long getTotalPhysicalMemorySize() {
        return ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean())
                .getTotalPhysicalMemorySize();
    }

    private long getFreePhysicalMemorySize() {
        return ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean())
                .getFreePhysicalMemorySize();
    }
}
