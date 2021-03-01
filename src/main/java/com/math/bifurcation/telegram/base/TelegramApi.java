package com.math.bifurcation.telegram.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Leonid Cheremshantsev
 */
public class TelegramApi {

    private final Logger log = LoggerFactory.getLogger(TelegramApi.class);
    private final TelegramLongPollingBot bot;


    public TelegramApi(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void sendReplyMessage(UpdateWrapper update, String text) {
        SendMessage message = buildTextMessage(update.getChatId(), text);
        message.setReplyToMessageId(update.getMessageId());

        sendMessge(message);
    }

    public void sendHtmlMessage(String chatId, String html) {
        SendMessage sendMessage = buildTextMessage(chatId, html);
        sendMessage.enableHtml(true);

        sendMessge(sendMessage);
    }

    public void sendTextMessage(String chatId, String text) {
        sendMessge(buildTextMessage(chatId, text));
    }

    private void sendMessge(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending message", e);
        }
    }

    private SendMessage buildTextMessage(String chatId, String text) {
        return new SendMessage(chatId, text);
    }
}
