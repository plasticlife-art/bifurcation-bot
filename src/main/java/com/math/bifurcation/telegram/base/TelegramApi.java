package com.math.bifurcation.telegram.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Leonid Cheremshantsev
 */
public class TelegramApi {

    private final Logger log = LoggerFactory.getLogger(TelegramApi.class);
    private final TelegramLongPollingBot bot;

    private final Map<String, String> cache = new HashMap<>();


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
        sendDocument(chatId, "static/stopwatch.gif");
    }

    public void sendDocument(String chatId, String path) {
        try {
            SendDocument sendDocument;

            if (isCached(path)) {
                sendDocument = new SendDocument(chatId, new InputFile(cache.get(path)));
            } else {
                sendDocument = new SendDocument(chatId, getFile(path));
            }

            Message message = bot.execute(sendDocument);
            if (!isCached(path)) {
                String fileId = message.getDocument().getFileId();
                cache(path, fileId);
            }
        } catch (URISyntaxException e) {
            log.error("Can't get document '{}' file from resources", path);
        } catch (TelegramApiException e) {
            log.error("Error while sending document");
        }
    }

    private void cache(String path, String fileId) {
        cache.put(path, fileId);
    }

    private boolean isCached(String path) {
        return cache.containsKey(path);
    }

    public void sendCoffeeSticker(String chatId) {
        sendSticker(chatId, "static/sticker.webp");
    }

    public void sendSticker(String chatId, String path) {
        try {
            SendSticker sendSticker;

            if (isCached(path)) {
                sendSticker = new SendSticker(chatId, new InputFile(cache.get(path)));
            } else {
                sendSticker = new SendSticker(chatId, getFile(path));
            }

            Message message = bot.execute(sendSticker);
            if (!isCached(path)) {
                String fileId = message.getSticker().getFileId();
                cache(path, fileId);
            }
        } catch (URISyntaxException e) {
            log.error("Can't get document '{}' file from resources", path);
        } catch (TelegramApiException e) {
            log.error("Error while sending document");
        }
    }

    private InputFile getFile(String path) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
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
