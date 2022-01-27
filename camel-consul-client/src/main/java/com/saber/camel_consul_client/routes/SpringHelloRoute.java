package com.saber.camel_consul_client.routes;

import com.saber.camel_consul_client.dto.HelloDto;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import javax.ws.rs.core.MediaType;

@Component
public class SpringHelloRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        rest("/spring")
                .get("/sayHello")
                .id(Routes.SPRING_HELLO_ROUTE)
                .description("spring say Hello")
                .produces(MediaType.APPLICATION_JSON)
                .responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(HelloDto.class).endResponseMessage()
                .param().name("firstName").type(RestParamType.query).dataType("string").required(true).example("saber").endParam()
                .param().name("lastName").type(RestParamType.query).dataType("string").required(true).example("Azizi").endParam()
                .enableCORS(true)
                .bindingMode(RestBindingMode.json)
                .route()
                .routeId(Routes.SPRING_HELLO_ROUTE)
                .routeGroup(Routes.SPRING_HELLO_ROUTE_GROUP)
                .to(String.format("direct:%s", Routes.SPRING_HELLO_ROUTE_GATEWAY));

        from(String.format("direct:%s", Routes.SPRING_HELLO_ROUTE_GATEWAY))
                .routeId(Routes.SPRING_HELLO_ROUTE_GATEWAY)
                .routeGroup(Routes.SPRING_HELLO_ROUTE_GROUP)
                .log("Request for spring sayHello with firstName : ${in.header.firstName} , lastName : ${in.header.lastName}")
                .serviceCall("{{service.external-service.name}}/{{service.external-service.sayHelloSpring}}?bridgeEndpoint=true")
                .convertBodyTo(String.class)
                .log("Response for say Hello ===> ${in.body}")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));


    }
}
