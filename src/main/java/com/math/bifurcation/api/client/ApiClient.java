package com.math.bifurcation.api.client;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * @author Leonid Cheremshantsev
 */
public abstract class ApiClient {

    private final RestTemplate restTemplate;

    protected ApiClient() {
        this.restTemplate = new RestTemplate();
    }

    protected <T> T get(String url, Class<T> resClass) {
        return restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                resClass).getBody();
    }
}
