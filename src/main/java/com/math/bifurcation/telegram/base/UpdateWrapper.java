package com.math.bifurcation.telegram.base;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Leonid Cheremshantsev
 */
public class UpdateWrapper {

    private final Update update;

    public UpdateWrapper(Update update) {
        this.update = update;
    }

    public boolean hasTextMessage() {
        return update.hasMessage() && getMessage(update).hasText();
    }

    public String getUsername() {
        return "@" + getMessage(update)
                .getChat()
                .getUserName();
    }

    public boolean isCommand(String command) {
        return getText().equals(command);
    }

    public String getText() {
        return getMessage(update).getText();
    }

    public Integer getMessageId() {
        return getMessage(update).getMessageId();
    }

    public Message getMessage(Update update) {
        return update.getMessage();
    }

    public String getChatId() {
        return String.valueOf(getChatIdLong());
    }

    public Long getChatIdLong() {
        return getMessage(update).getChatId();
    }
}
