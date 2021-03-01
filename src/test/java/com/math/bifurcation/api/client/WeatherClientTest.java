package com.math.bifurcation.api.client;

import com.math.bifurcation.BifurcationBotApplication;
import com.math.bifurcation.dto.weather.Weather;
import com.math.bifurcation.config.BeansConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
 * @author Leonid Cheremshantsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BeansConfig.class, BifurcationBotApplication.class})
public class WeatherClientTest {

    @Value("${weather.token}")
    private String token;
    @Autowired
    private com.math.bifurcation.api.client.WeatherClient client;


    @Test
    public void getCurrentWeather() {
        Weather currentWeather = client.getWeather();
        assertNotNull(currentWeather);
        assertNotNull(currentWeather.currentWeather);
    }
}