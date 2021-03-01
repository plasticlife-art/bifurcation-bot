package com.math.bifurcation.telegram.base.handler;

import com.math.bifurcation.telegram.base.UpdateWrapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * @author Leonid Cheremshantsev
 */

@Service
public class EchoHandler extends Handler {

    public EchoHandler(MessageSource messages) {
        super(messages);
    }

    @Override
    public boolean support(UpdateWrapper update) {
        return update.hasTextMessage();
    }

    @Override
    public void handle(UpdateWrapper update) {
        sendEcho(update);
    }

    private void sendEcho(UpdateWrapper update) {
        String echo = buildEchoMessage(update);

        api.sendReplyMessage(update, echo);

        log.info("Sent echo: {}", echo);
    }

    private String buildEchoMessage(UpdateWrapper update) {
        return String.format(getMessage("handler.echo.pattern"), update.getUsername(), update.getText());
    }

}
