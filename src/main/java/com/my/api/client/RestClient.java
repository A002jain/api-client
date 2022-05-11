package com.my.api.client;

import com.my.api.auth.ApiAuth;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class RestClient {

  private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
  private HttpRequest<Buffer> httpRequest;
  public static final String RESPONSE = "response = {}";
  public static final String ERROR = "Error = {}";
  private final WebClient webClient;

  public RestClient(WebClient webClient){
    this.webClient = webClient;
  }

  public RestClient withAuth(ApiAuth auth){
    httpRequest.authentication(auth.getAuth());
    return this;
  }


  public Map<String, Object> execute() {
    CompletableFuture<JsonObject> response = new CompletableFuture<>();
    try {
      httpRequest.send()
              .onSuccess(res -> {
                try {
                  response.complete(res.bodyAsJsonObject());
                  logger.info(RESPONSE, res.bodyAsJsonObject().encodePrettily());
                } catch (Exception e) {
                  response.completeExceptionally(e);
                  logger.info(e.getMessage());
                }
              })
              .onFailure(res -> {
                response.completeExceptionally(res.getCause());
                logger.info(ERROR, res.getMessage());
              });
      response.whenComplete((tResponse, throwable) -> {
        if (response.isCancelled()) {
          response.cancel(true);
        }
      });
      return response.get().getMap();
    } catch (InterruptedException | ExecutionException e) {
      return new JsonObject().put("Error", e.getMessage()).getMap();
    }
  }

  public void executeAsync(){
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
