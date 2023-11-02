package ru.kata.spring.boot_security.demo.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@RestController
@RequestMapping("/user/api")
public class RESTUsersController {

    private final UserService userService;

    @Autowired
    public RESTUsersController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public ResponseEntity<User> show(Model model) {
       User user = userService.show(model);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
