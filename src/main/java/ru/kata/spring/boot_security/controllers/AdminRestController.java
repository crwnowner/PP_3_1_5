package ru.kata.spring.boot_security.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.entities.User;
import ru.kata.spring.boot_security.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User user = userService.getById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }

    @PostMapping("/new")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        System.out.println(user);
        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(id, user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
