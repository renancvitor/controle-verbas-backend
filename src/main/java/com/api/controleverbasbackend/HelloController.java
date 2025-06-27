package com.api.controleverbasbackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello1")
public class HelloController {

    @GetMapping
    public String helloWorld() {
        return "Hello World";
    }
}
