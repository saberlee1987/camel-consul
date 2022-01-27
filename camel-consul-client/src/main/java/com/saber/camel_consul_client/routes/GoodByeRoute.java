package com.saber.camel_consul_client.routes;

import com.saber.camel_consul_client.dto.HelloDto;
import com.saber.camel_consul_client.dto.PersonDto;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import javax.ws.rs.core.MediaType;

@Component
public class GoodByeRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        rest("/goodbye")
                .post("/sayGoodbye")
                .id(Routes.GOODBYE_ROUTE)
                .description("say GoodBye")
                .produces(MediaType.APPLICATION_JSON)
                .responseMessage().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(HelloDto.class).endResponseMessage()
                .enableCORS(true)
                .type(PersonDto.class)
                .bindingMode(RestBindingMode.json)
                .route()
                .routeId(Routes.GOODBYE_ROUTE)
                .routeGroup(Routes.GOODBYE_ROUTE_GROUP)
                .to(String.format("direct:%s", Routes.GOODBYE_ROUTE_GATEWAY));

        from(String.format("direct:%s", Routes.GOODBYE_ROUTE_GATEWAY))
                .routeId(Routes.GOODBYE_ROUTE_GATEWAY)
                .routeGroup(Routes.GOODBYE_ROUTE_GROUP)
                .log("Request for GoodBye with body ${in.body}")
                .marshal().json(JsonLibrary.Jackson,PersonDto.class)
                .serviceCall("{{service.external-service.name}}/{{service.external-service.goodbye}}?bridgeEndpoint=true")
                .convertBodyTo(String.class)
                .log("Response for say GoodBye ===> ${in.body}")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));


    }
}
