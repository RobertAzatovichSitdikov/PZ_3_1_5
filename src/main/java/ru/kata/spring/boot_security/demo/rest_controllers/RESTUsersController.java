package ru.kata.spring.boot_security.demo.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/user/api")
public class RESTUsersController {

    private final UserService userService;

    @Autowired
    public RESTUsersController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public String show(Model model) {
        model.addAttribute("user", userService.show(model));
        return "bootstrap/user";
    }

}
