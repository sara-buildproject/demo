package com.buildsessions.stockmarketanalyzer.api;

import com.buildsessions.stockmarketanalyzer.entity.CustomUser;
import com.buildsessions.stockmarketanalyzer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CustomUser> createUser(@RequestBody CustomUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        CustomUser createdUser = userService.addUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
