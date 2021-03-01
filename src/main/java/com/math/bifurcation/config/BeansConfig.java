package com.math.bifurcation.config;

import com.math.bifurcation.telegram.TelegramBot;
import com.math.bifurcation.telegram.base.handler.Handler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.List;

/**
 * @author Leonid Cheremshantsev
 */
@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("credentials.properties")
})
public class BeansConfig {

    private final List<Handler> handlers;

    public BeansConfig(List<Handler> handlers) {
        this.handlers = handlers;
    }


    @Bean
    public TelegramBot telegramBot(@Value("${bot.token}") String token) {
        return new TelegramBot(token, handlers);
    }
}
