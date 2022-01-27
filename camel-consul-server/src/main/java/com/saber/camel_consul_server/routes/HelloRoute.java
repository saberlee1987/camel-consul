package com.saber.camel_consul_server.routes;

import com.saber.camel_consul_server.dto.HelloDto;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
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
                .get("/sayHello/{nationalCode}")
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
                .log("Request for sayHello with firstName : ${in.header.firstName} , lastName : ${in.header.lastName}")
                .process(exchange -> {
                    String firstName = exchange.getIn().getHeader("firstName", String.class);
                    String lastName = exchange.getIn().getHeader("lastName", String.class);
                    String nationalCode = exchange.getIn().getHeader("nationalCode", String.class);
                    String message = String.format("Hello %s %s with nationalCode %s", firstName, lastName,nationalCode);
                    HelloDto helloDto = new HelloDto();
                    helloDto.setMessage(message);
                    exchange.getIn().setBody(helloDto);
                })
                .log("Response for say Hello ===> ${in.body}")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));


    }
}
