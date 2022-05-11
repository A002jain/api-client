package com.my.api.client;

import com.my.api.config.ApiConfig;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HttpClient {

  private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

  private static final Vertx vertx = Vertx.vertx();
  private static final WebClient webClient = WebClient.create(vertx, ApiConfig.apiConfig());
  private static final RestClient restClient = new RestClient(webClient);
  private static final GraphqlClient graphqlClient = new GraphqlClient(webClient);
  
  public static RestClient restClient(){
    return restClient;
  }

  public static GraphqlClient graphqlClient(){ return graphqlClient; }
  
}
