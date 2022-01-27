package com.saber.camel_consul_client.routes;

public interface Routes {
    String HELLO_ROUTE = "hello-route";
    String HELLO_ROUTE_GROUP = "hello-route-group";
    String HELLO_ROUTE_GATEWAY = "hello-route-gateway";

    String SPRING_HELLO_ROUTE = "spring-hello-route";
    String SPRING_HELLO_ROUTE_GROUP = "spring-hello-route-group";
    String SPRING_HELLO_ROUTE_GATEWAY = "spring-hello-route-gateway";

    String GOODBYE_ROUTE = "goodbye-route";
    String GOODBYE_ROUTE_GROUP = "goodbye-route-group";
    String GOODBYE_ROUTE_GATEWAY = "goodbye-route-gateway";


    String CONSUL_REGISTRATION_ROUTE = "consul-registration-route";
    String CONSUL_REGISTRATION_ROUTE_GROUP = "consul-registration-route-group";
}
