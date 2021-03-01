package com.math.bifurcation.telegram.base.handler;

import com.math.bifurcation.App;
import com.math.bifurcation.telegram.base.UpdateWrapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * @author Leonid Cheremshantsev
 */
@Service
public class UptimeHandler extends Handler {

    private final App app;

    public UptimeHandler(MessageSource messages, App app) {
        super(messages);
        this.app = app;
    }

    @Override
    public boolean support(UpdateWrapper update) {
        return isCommand(update, getMessage("handler.uptime.command"));
    }

    @Override
    public void handle(UpdateWrapper update) {
        api.sendTextMessage(update.getChatId(), getText());
    }

    private String getText() {
        return String.format(getMessage("handler.uptime.pattern"), app.getUptime());
    }
}
