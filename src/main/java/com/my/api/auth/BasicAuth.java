package com.my.api.auth;

import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;

import java.util.Optional;


public class BasicAuth implements ApiAuth{

    public Credentials getAuth(){
        final String USERNAME = Optional.of(System.getenv("user_name")).orElseThrow(RuntimeException::new);
        final String PASSWORD = Optional.of(System.getenv("password")).orElseThrow(RuntimeException::new);
        return new UsernamePasswordCredentials(USERNAME, PASSWORD);
    }
}
