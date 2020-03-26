package com.dql.shiro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRoleController {

    @GetMapping("/cat")
    public String cat(){
        return "cat";
    }

    @GetMapping("/dog")
    public String dog(){
        return "dog";
    }

    @GetMapping("/sing")
    public String sing(){
        return "sing";
    }

    @GetMapping("/jump")
    public String jump(){
        return "jump";
    }

    @GetMapping("/rap")
    public String rap(){
        return "rap";
    }

    @GetMapping("/basketball")
    public String basketball(){
        return "basketball";
    }

}
