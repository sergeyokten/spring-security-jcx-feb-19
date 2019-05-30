package com.oktenweb.springsecurityjcxfeb19.controllers;

import com.oktenweb.springsecurityjcxfeb19.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/users")
    public List<User> users() {
        return Arrays.asList(new User("asd", "qew"), new User("zxc", "poi"));
    }
}
