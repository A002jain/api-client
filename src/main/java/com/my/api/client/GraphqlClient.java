package com.my.api.client;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;


public class GraphqlClient {

  private static final Logger logger = LoggerFactory.getLogger(GraphqlClient.class);
  private RestClient restClient;

  private final GraphqlPayloadBuilder graphqlPayloadBuilder = new GraphqlPayloadBuilder();

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

  public GraphqlClient withAuth(){
    restClient = restClient.withBasicAuth();
    return this;
  }

  public void execute(){
    JsonObject request = graphqlPayloadBuilder.getJsonObject();
    execute(request);
    graphqlPayloadBuilder.clear();
  }

  public void execute(JsonObject request){
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
