package com.math.bifurcation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;

import java.io.File;

/**
 * @author Leonid Cheremshantsev
 */
public class DiskHealthIndicator extends HealthIndicator {

    private final Logger log = LoggerFactory.getLogger(DiskHealthIndicator.class);

    public DiskHealthIndicator() {
        super(null, null);
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        File root = new File("/");
        long freeMemory = root.getFreeSpace();
        long totalMemory = root.getTotalSpace();
        long allocatedMemory = totalMemory - freeMemory;

        if (freeMemory > 0) {
            builder.up();
        } else {
            log.warn(String.format("Free disk space below threshold. Available: %s", toHuman(freeMemory)));
            builder.down();
        }
        builder.withDetail("total", toHuman(totalMemory))
                .withDetail("used", toHuman(allocatedMemory))
                .withDetail("free", toHuman(freeMemory));
    }

}
