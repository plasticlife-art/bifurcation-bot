package com.math.bifurcation.health;

import com.math.bifurcation.App;
import org.springframework.boot.actuate.health.Health;

/**
 * @author Leonid Cheremshantsev
 */

public class UptimeHealthIndicator implements org.springframework.boot.actuate.health.HealthIndicator {

    private final App app;

    public UptimeHealthIndicator(App app) {
        this.app = app;
    }

    @Override
    public Health health() {
        Health.Builder health = Health.up();

        addUptime(health);

        return health.build();
    }

    private void addUptime(Health.Builder health) {
        health.withDetail("time", app.getUptime());
    }
}
