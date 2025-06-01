package com.project.ecommerce.controllers;

import com.project.ecommerce.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/hello")
    public Message sayMessage(){
        return new Message("Hello World");
    }


}
