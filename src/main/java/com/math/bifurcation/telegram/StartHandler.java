package com.math.bifurcation.telegram;

import com.math.bifurcation.data.user.User;
import com.math.bifurcation.data.user.UserRepository;
import com.math.bifurcation.telegram.base.UpdateWrapper;
import com.math.bifurcation.telegram.base.handler.Handler;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
public class StartHandler extends Handler {

    private final UserRepository userRepository;

    public StartHandler(UserRepository userRepository, MessageSource messages) {
        super(messages);
        this.userRepository = userRepository;
    }

    public boolean support(UpdateWrapper update) {
        return update.isCommand(getMessage("handler.start.command"));
    }

    @Override
    public void handle(UpdateWrapper update) throws IOException {
        saveUser(update);
    }

    private void saveUser(UpdateWrapper update) throws IOException {
        userRepository.add(buildUser(update));
        sendHello(update);
    }

    private void sendHello(UpdateWrapper update) {
        api.sendReplyMessage(update, getMessage("handler.start.greeting"));
    }

    private User buildUser(UpdateWrapper update) {
        return User.builder()
                .id(update.getChatIdLong())
                .username(update.getUsername())
                .create_ts(new Date())
                .build();
    }

}