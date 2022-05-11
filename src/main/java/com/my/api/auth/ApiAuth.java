package com.my.api.auth;

import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;

import java.util.Optional;

public class ApiAuth {

    private static final String USERNAME = Optional.of(System.getenv("user_name")).orElseThrow(RuntimeException::new);
    private static final String PASSWORD = Optional.of(System.getenv("password")).orElseThrow(RuntimeException::new);

    public static Credentials getBasicAuth(){
        return new UsernamePasswordCredentials(USERNAME, PASSWORD);
    }
}
