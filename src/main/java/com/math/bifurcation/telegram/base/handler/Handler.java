package com.math.bifurcation.telegram.base.handler;

import com.math.bifurcation.telegram.base.TelegramApi;
import com.math.bifurcation.telegram.base.UpdateWrapper;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Leonid Cheremshantsev
 */
public abstract class Handler {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MessageSource messages;

    @Setter
    protected TelegramApi api;

    protected Handler(MessageSource messages) {
        this.messages = messages;
    }

    protected ScheduledExecutorService initScheduler() {
        return Executors.newScheduledThreadPool(3);
    }

    public abstract boolean support(UpdateWrapper update);

    public abstract void handle(UpdateWrapper update) throws IOException;

    protected boolean isCommand(UpdateWrapper update, String command) {
        return update.hasTextMessage() && update.isCommand(command);
    }

    protected void sleepSeconds(int seconds) throws InterruptedException {
        TimeUnit.SECONDS.sleep(seconds);
    }

    //todo use spring method args instead of String.format
    protected String getMessage(String key) {
        return messages.getMessage(key, null, Locale.US);
    }
}
