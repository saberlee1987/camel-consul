package com.saber.camel_consul_client.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RouteDefinition extends RouteBuilder {

    @Value(value = "${service.api.base-path}")
    private String apiBasePath;
    @Value(value = "${service.swagger.path}")
    private String swaggerPath;
    @Value(value = "${service.swagger.title}")
    private String swaggerTitle;
    @Value(value = "${service.swagger.version}")
    private String swaggerVersion;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .contextPath(apiBasePath)
                .apiContextPath(swaggerPath)
                .component("servlet")
                .apiProperty("api.title", swaggerTitle)
                .apiProperty("api.version", swaggerVersion)
                .apiProperty("cors", "true")
                .enableCORS(true)
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");
    }
}
