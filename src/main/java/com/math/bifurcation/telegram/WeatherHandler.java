package com.math.bifurcation.telegram;

import com.math.bifurcation.api.client.WeatherClient;
import com.math.bifurcation.data.user.User;
import com.math.bifurcation.data.user.UserRepository;
import com.math.bifurcation.dto.weather.Weather.CurrentWeather;
import com.math.bifurcation.telegram.base.UpdateWrapper;
import com.math.bifurcation.telegram.base.handler.Handler;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
public class WeatherHandler extends Handler {

    private final ScheduledExecutorService executorService;
    private final UserRepository userRepository;
    private final WeatherClient weatherClient;

    public WeatherHandler(UserRepository userRepository, WeatherClient weatherClient, MessageSource messages) {
        super(messages);
        this.userRepository = userRepository;
        this.weatherClient = weatherClient;
        this.executorService = initScheduler();

        initBrodcast();
    }

    private void initBrodcast() {
        executorService.scheduleAtFixedRate(this::sendBroadcast, 0, 30, TimeUnit.MINUTES);
    }

    private void sendBroadcast() {
        Set<User> users = userRepository.findAll();
        for (User user : users) {
            sendWeather(user.getChatId());
        }
        log.info("Broadcast sent to {} users", users.size());
    }

    private void sendWeather(String chatId) {
        api.sendHtmlMessage(chatId, getCurrentWeather());
    }

    private String getCurrentWeather() {
        CurrentWeather currentWeather = weatherClient.getCurrentWeather();

        return String.format(getMessage("handler.weather.pattern"),
                currentWeather.temp,
                currentWeather.feelsLike,
                currentWeather.tempMin,
                currentWeather.tempMax,
                currentWeather.pressure,
                currentWeather.humidity);
    }

    @Override
    public boolean support(UpdateWrapper update) {
        return isCommand(update, getMessage("handler.weather.command"));
    }

    @Override
    public void handle(UpdateWrapper update) {
        sendWeather(update.getChatId());
    }
}