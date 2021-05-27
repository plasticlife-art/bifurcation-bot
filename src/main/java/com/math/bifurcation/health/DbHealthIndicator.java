package com.math.bifurcation.health;

import com.math.bifurcation.data.user.UserRepository;
import org.springframework.boot.actuate.health.Health;

/**
 * @author Leonid Cheremshantsev
 */

public class DbHealthIndicator implements org.springframework.boot.actuate.health.HealthIndicator {

    private final UserRepository userRepository;

    public DbHealthIndicator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Health health() {
        Health.Builder health = Health.up();

        addUserCount(health);

        return health.build();
    }

    private void addUserCount(Health.Builder health) {
        health.withDetail("users", userRepository.getCount());
    }
}
