package com.math.bifurcation.telegram;

import com.math.bifurcation.telegram.base.TelegramApi;
import com.math.bifurcation.telegram.base.UpdateWrapper;
import com.math.bifurcation.telegram.base.handler.Handler;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.List;

/**
 * @author Leonid Cheremshantsev
 */
public class TelegramBot extends TelegramLongPollingBot {

    public static final String BIFURCATION_BOT_USERNAME = "bifurcation_bot";
    private final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final String token;
    private final List<Handler> handlers;

    public TelegramBot(String token, List<Handler> handlers) {
        this.token = token;

        initHandlers(handlers);
        this.handlers = handlers;
    }

    private void initHandlers(List<Handler> handlers) {
        for (Handler handler : handlers) {
            handler.setApi(new TelegramApi(this));
        }
    }

    @Override
    public String getBotUsername() {
        return BIFURCATION_BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update recived: " + update.toString());
        handle(new UpdateWrapper(update));
    }

    private void handle(UpdateWrapper update) throws IOException {
        for (Handler handler : handlers) {
            if (handler.support(update)) {
                handler.handle(update);
                return;
            }
        }
    }
}
