package com.my.api.auth;

import io.vertx.ext.auth.authentication.Credentials;

public interface ApiAuth {
    Credentials getAuth();
}
