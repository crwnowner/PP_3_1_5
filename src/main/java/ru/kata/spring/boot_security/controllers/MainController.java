package ru.kata.spring.boot_security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.entities.User;
import ru.kata.spring.boot_security.services.UserService;

import java.security.Principal;

@Controller
public class MainController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        model.addAttribute("authUser", userService.findByUsername(principal.getName()));
        model.addAttribute("isAdmin", userService.hasAdminRole());
        return "userPage";
    }

    @GetMapping("/admin")
    public String usersList(Model model, Principal principal) {
        model.addAttribute("authUser", userService.findByUsername(principal.getName()));
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }
}
