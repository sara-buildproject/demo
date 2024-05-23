package com.buildsessions.stockmarketanalyzer.api;

import com.buildsessions.stockmarketanalyzer.entity.CustomUser;
import com.buildsessions.stockmarketanalyzer.entity.Stock;
import com.buildsessions.stockmarketanalyzer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CustomUser> createUser(@RequestBody CustomUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        CustomUser createdUser = userService.addUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomUser> getUserById(@PathVariable Long id) {
        CustomUser user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}/monitored-stocks")
    public ResponseEntity<Set<Long>> getMoniteredStockIdsForUser(@PathVariable Long userId) {
        Set<Long> monitoredStockIds = userService.getMonitoredStockIdsForUser(userId);
        if (monitoredStockIds != null) {
            return new ResponseEntity<>(monitoredStockIds, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}/add-stock")
    public ResponseEntity<Void> addStockToUser(@PathVariable Long userId, @RequestParam String symbol) {
        userService.addStockToUser(userId, symbol);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{userId}/remove-stock")
    public ResponseEntity<Void> removeStockFromUser(@PathVariable Long userId, @RequestParam String symbol) {
        userService.removeStockFromUser(userId, symbol);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
