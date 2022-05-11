# api-client
An api client which allow us to call rest or graphql api from java


## USAGE

1. set enviroment variable
   - `host=<api port>`
   - `base_url=<base url>` default local host
   -  for basic auth env -> `user_name=<user_name>` and `password=<password>` 
2. include api-client dependency
   - Will Work on it
3. code usage
   - rest api
   
    ```text
    HttpClient.restClient()
        .get("/hello")
        .executeAsync();
          
    // with basic auth  
    
    HttpClient.restClient()
        .get("/hello")
    //    .withAuth(new BasicAuth()) use env vars to pass username and password
        .withAuth(new UsernamePasswordCredentials(USERNAME, PASSWORD))
        .executeAsync();
          
    ```
    - graphql api
   
    ```text
    HttpClient.graphqlClient()
        .query("query{ allUser{ name } }")
        .variable("has","enable")
        .variable("not","enable")
        .executeAsync();
    
    // with basic auth
    
    HttpClient.graphqlClient()
        .query("query{ allUser{ name } }")
    //    .withAuth(new BasicAuth()) use env vars to pass username and password
        .withAuth(new UsernamePasswordCredentials(USERNAME, PASSWORD))
        .variable("hash","enable")
        .variable("not","enable")
        .executeAsync();
    ```