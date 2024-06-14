package com.buildsessions.stockmarketanalyzer.api;

import com.buildsessions.stockmarketanalyzer.entity.CustomUser;
import com.buildsessions.stockmarketanalyzer.entity.Stock;
import com.buildsessions.stockmarketanalyzer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody CustomUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        CustomUser createdUser = userService.addUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", createdUser.getId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody CustomUser user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUser authenticatedUser = userService.findByUsername(user.getUsername());
            String token = "dummy-token";  // Placeholder token

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", authenticatedUser.getId());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
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
    public ResponseEntity<List<Stock>> getMonitoredStocksForUser(@PathVariable Long userId) {
        List<Stock> monitoredStocks = userService.getMonitoredStocksForUser(userId);
        if (monitoredStocks != null) {
            return new ResponseEntity<>(monitoredStocks, HttpStatus.OK);
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
