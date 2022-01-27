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
public class HelloRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        rest("/hello")
                .get("/sayHello")
                .id(Routes.HELLO_ROUTE)
                .description("say Hello")
                .produces(MediaType.APPLICATION_JSON)
                .responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(HelloDto.class).endResponseMessage()
                .param().name("firstName").type(RestParamType.query).dataType("string").required(true).example("saber").endParam()
                .param().name("lastName").type(RestParamType.query).dataType("string").required(true).example("Azizi").endParam()
                .param().name("nationalCode").type(RestParamType.header).dataType("string").required(true).example("0079028748").endParam()
                .enableCORS(true)
                .bindingMode(RestBindingMode.json)
                .route()
                .routeId(Routes.HELLO_ROUTE)
                .routeGroup(Routes.HELLO_ROUTE_GROUP)
                .to(String.format("direct:%s", Routes.HELLO_ROUTE_GATEWAY));

        from(String.format("direct:%s", Routes.HELLO_ROUTE_GATEWAY))
                .routeId(Routes.HELLO_ROUTE_GATEWAY)
                .routeGroup(Routes.HELLO_ROUTE_GROUP)
                .log("Request for sayHello with firstName : ${in.header.firstName} , lastName : ${in.header.lastName} , nationalCode : ${in.header.nationalCode}")
                .serviceCall("{{service.external-service.name}}/{{service.external-service.sayHello}}/${in.header.nationalCode}?bridgeEndpoint=true")
                .convertBodyTo(String.class)
                .log("Response for say Hello ===> ${in.body}")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));


    }
}
