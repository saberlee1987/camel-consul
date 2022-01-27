package com.saber.camel_consul_server.controllers;

import com.saber.camel_consul_server.dto.HelloDto;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/services/spring")
@Tags(value = {
        @Tag(name = "Hello service",description = "Hello Service")
})
public class HelloController {

    @GetMapping(value = "/hello")
    public ResponseEntity<HelloDto> hello(@RequestParam @Schema(name = "firstName",title = "firstName",example = "saber",required = true) String firstName ,
                                          @RequestParam @Schema(name = "lastName",title = "lastName",example = "Azizi",required = true) String lastName){
        String message = String.format("Hello %s %s", firstName, lastName);
        HelloDto helloDto = new HelloDto();
        helloDto.setMessage(message);
        return ResponseEntity.ok(helloDto);
    }
}
