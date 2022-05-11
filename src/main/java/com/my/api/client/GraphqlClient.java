package com.my.api.client;

import com.my.api.auth.ApiAuth;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;


public class GraphqlClient {


  private static final Logger logger = LoggerFactory.getLogger(GraphqlClient.class);
  private final GraphqlPayloadBuilder graphqlPayloadBuilder = new GraphqlPayloadBuilder();
  private static JsonObject response = new JsonObject();
  private RestClient restClient;

  public GraphqlClient(WebClient webClient){
    this.restClient = new RestClient(webClient);
  }

  public GraphqlClient query(String query){
    restClient = restClient.post("/graphql");
    graphqlPayloadBuilder.query(query);
    return this;
  }

  public GraphqlClient mutation(String mutation){
    restClient = restClient.post("/graphql");
    graphqlPayloadBuilder.mutation(mutation);
    return this;
  }

  public GraphqlClient variable(String key, String value){
    graphqlPayloadBuilder.variable(key, value);
    return this;
  }

  public GraphqlClient withAuth(ApiAuth auth){
    restClient = restClient.withAuth(auth);
    return this;
  }
  public void executeAsync(){
    JsonObject request = graphqlPayloadBuilder.getJsonObject();
    executeAsync(request);
    graphqlPayloadBuilder.clear();
  }

  public void executeAsync(JsonObject request){
    restClient.getHttpRequest()
            .as(BodyCodec.jsonObject())
            .sendJsonObject(request, ar -> {
              if (ar.succeeded()) {
                JsonObject response = ar.result().body();
                logger.info("response = {}", response.encodePrettily());
              } else {
                ar.cause().printStackTrace();
              }
            });
  }

  public Map<String, Object> execute(){
    JsonObject request = graphqlPayloadBuilder.getJsonObject();
    JsonObject jsonResponse = execute(request);
    graphqlPayloadBuilder.clear();
    return jsonResponse.getMap();
  }

  public JsonObject execute(JsonObject request) {
    CompletableFuture<JsonObject> response = new CompletableFuture<>();
    try {
      restClient.getHttpRequest()
              .as(BodyCodec.jsonObject())
              .sendJsonObject(request, ar -> {
                if (ar.succeeded()) {
                  response.complete(ar.result().body());
                  logger.info("response = {}", ar.result().body().encodePrettily());
                } else {
                  response.completeExceptionally(ar.cause());
                }
              });
      response.whenComplete((tResponse, throwable) -> {
        if (response.isCancelled()) {
          response.cancel(true);
        }
      });
      return response.get();
    } catch (InterruptedException | ExecutionException e) {
      return new JsonObject().put("Error", e.getMessage());
    }
  }


  static class GraphqlPayloadBuilder{

    private static final String QUERY = "query";
    private static final String MUTATION = "mutation";
    private static final String VARIABLES = "variables";
    private final JsonObject jsonObject = new JsonObject();

    public void query(String query){
      jsonObject.put(QUERY,query);
    }

    public void mutation(String mutation){
      jsonObject.put(MUTATION,mutation);
    }

    public void variable(String key , String value){
      HashMap<String, String>  variableMap;
      if(jsonObject.getMap().containsKey(VARIABLES)){
        variableMap = (HashMap<String, String>) jsonObject.getMap().get(VARIABLES);
      }
      else{
        variableMap = new HashMap<>();
      }
      variableMap.put(key, value);
      jsonObject.put(VARIABLES, variableMap);
    }

    public JsonObject getJsonObject(){
      return jsonObject;
    }

    public void clear(){
      jsonObject.clear();
    }
  }
}
