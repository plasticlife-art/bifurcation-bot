package com.math.bifurcation;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Service
public class App {

    private final MessageSource messages;

    public App(MessageSource messages) {
        this.messages = messages;
    }

    public String getUptime() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        long hours = TimeUnit.MILLISECONDS.toHours(uptime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(uptime) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);
        return format(hours, minutes, seconds);
    }

    private String format(long hours, long minutes, long seconds) {
        return String.format(getMessage(), hours, minutes, seconds);
    }

    private String getMessage() {
        return messages.getMessage("app.uptime.pattern", null, Locale.US);
    }
}