package com.math.bifurcation.telegram.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;

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

    public void sendButton(String chatId, String messageText, String buttonText) {
        SendMessage message = new SendMessage(chatId, messageText);

        KeyboardButton startButton = new KeyboardButton(buttonText);

        KeyboardRow keyRow = new KeyboardRow();

        keyRow.add(startButton);

        message.setReplyMarkup(new ReplyKeyboardMarkup(Collections.singletonList(keyRow)));

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending button", e);
        }
    }


    public void sendRemoveKeyboard(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);

        message.setReplyMarkup(new ReplyKeyboardRemove(true));

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending button", e);
        }
    }

    public void sendCountDown(String chatId) {
        try {
            SendDocument sendDocument = new SendDocument(chatId, new InputFile("CgACAgIAAxkBAAIapmBFR6QB3ZT5wMzEsp4ja3pexPTDAAJ2DQACTdgpSvk32b6nG-OTHgQ"));

            bot.execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error("Error while sending message", e);
        }
    }

    public void sendCoffeeSticker(String chatId) {
        try {
            SendSticker sendSticker = new SendSticker(chatId, new InputFile("CAACAgIAAxkBAAIawWBFSWp7uH2h32xxyWZMkwZo8zXNAAKADQACTdgpSiFLgd5kx82NHgQ"));

            bot.execute(sendSticker);
        } catch (TelegramApiException e) {
            log.error("Error while sending message", e);
        }
    }

    private InputFile getFile(String path) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());
            new InputFile("name");
            return new InputFile(new File(resource.toURI()));
        }
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
