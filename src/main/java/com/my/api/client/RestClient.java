package com.my.api.client;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RestClient {

  private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
  private HttpRequest<Buffer> httpRequest;
  public static final String RESPONSE = "response = {}";
  public static final String ERROR = "Error = {}";
  private final WebClient webClient;

  public RestClient(WebClient webClient){
    this.webClient = webClient;
  }

  private Credentials getBasicAuth(){
    return new UsernamePasswordCredentials("test","test");
  }

  public RestClient withBasicAuth(){
    httpRequest.authentication(getBasicAuth());
    return this;
  }

  public void execute(){
    httpRequest.send()
      .onSuccess(res -> {
        try {
          logger.info(RESPONSE, res.bodyAsJsonObject().encodePrettily());
        }catch (Exception e){
          logger.info(e.getMessage());
        }
      })
      .onFailure(res -> logger.info(ERROR, res.getMessage()));
  }

  public RestClient get(String path){
    httpRequest = webClient.get(path);
    return this;
  }

  public RestClient post(String path){
    httpRequest = webClient.post(path);
    return this;
  }

  public RestClient put(String path){
    httpRequest = webClient.put(path);
    return this;
  }

  public RestClient delete(String path){
    httpRequest = webClient.delete(path);
    return this;
  }

  public HttpRequest<Buffer> getHttpRequest(){
    return httpRequest;
  }


}
