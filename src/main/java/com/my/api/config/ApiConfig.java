package com.my.api.config;

import io.vertx.ext.web.client.WebClientOptions;

import java.util.Optional;

public class ApiConfig {

    private static final Integer PORT = Integer.parseInt(Optional.of(System.getenv("port")).orElseThrow(RuntimeException::new));
    private static final String BASE_URL = Optional.of(System.getenv("base_url")).orElse("localhost");


    public static WebClientOptions apiConfig(){
        return new WebClientOptions().setDefaultHost(BASE_URL).setDefaultPort(PORT);
    }
}
