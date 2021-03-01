package com.math.bifurcation;

import com.math.bifurcation.data.user.UserRepository;
import com.math.bifurcation.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.sql.DataSource;
import java.util.Objects;

@SpringBootApplication
public class BifurcationBotApplication {

	@Autowired
	private Environment env;

	public static void main(String[] args) throws TelegramApiException {
		ConfigurableApplicationContext context = initContext(args);

		initBot(context);
	}

	private static ConfigurableApplicationContext initContext(String[] args) {
		SpringApplication app = new SpringApplication(BifurcationBotApplication.class);
		app.setApplicationStartup(new BufferingApplicationStartup(2048));
		return app.run(args);
	}

	private static void initBot(ConfigurableApplicationContext context) throws TelegramApiException {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		telegramBotsApi.registerBot(context.getBean(TelegramBot.class));
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setCacheSeconds(10); //reload messages every 10 seconds
		return messageSource;
	}

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
		return dataSource;
	}

	@Bean
	public org.springframework.boot.actuate.health.HealthIndicator dbHealthIndicator(UserRepository userRepository) {
		return new DbHealthIndicator(userRepository);
	}

	@Bean
	public DiskSpaceHealthIndicator ramHealthIndicator() {
		return new MemoryHealthIndicator();
	}

	@Bean
	public DiskHealthIndicator diskHealthIndicator() {
		return new DiskHealthIndicator();
	}

	@Bean
	public UptimeHealthIndicator uptimeHealthIndicator(App app) {
		return new UptimeHealthIndicator(app);
	}

}
