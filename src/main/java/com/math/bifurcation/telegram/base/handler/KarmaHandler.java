package com.math.bifurcation.telegram.base.handler;

import com.math.bifurcation.telegram.base.UpdateWrapper;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;


@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
public class KarmaHandler extends Handler {

    private final Random random = new Random();
    private final ScheduledExecutorService executorService;

    public KarmaHandler(MessageSource messages) {
        super(messages);
        this.executorService = initScheduler();
    }


    String getKarma() {
        return random.nextBoolean()
                ? getMessage("handler.karma.true")
                : getMessage("handler.karma.false");
    }

    @Override
    public boolean support(UpdateWrapper update) {
        return isCommand(update, getMessage("handler.karma.command"));
    }

    @Override
    public void handle(UpdateWrapper update) {
        sendKarma(update);
    }

    private void sendKarma(UpdateWrapper update) {
        executorService.execute(() -> {
            try {
                api.sendTextMessage(update.getChatId(), getFirstMessage());
                sleepSeconds(5);

                api.sendTextMessage(update.getChatId(), getSecondMessage());
                sleepSeconds(10);

                String karma = getKarma();
                api.sendTextMessage(update.getChatId(), getResultMessage(karma));

                log.info("Sent karma '{}' to @{}", karma, update.getUsername());
            } catch (InterruptedException e) {
                log.error("Error while sending karma", e);
            }
        });
    }

    private String getResultMessage(String karma) {
        return String.format(getMessage("handler.karma.pattern"), karma);
    }

    private String getSecondMessage() {
        return getMessage("handler.karma.message.second");
    }

    private String getFirstMessage() {
        return getMessage("handler.karma.message.first");
    }

}