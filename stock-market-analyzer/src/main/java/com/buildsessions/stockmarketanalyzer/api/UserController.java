package com.buildsessions.stockmarketanalyzer.api;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/user/home")
    public String userHomePage() {
        return "user_home";
    }

    @GetMapping("/admin/home")
    public String adminHomePage() {
        return "admin_home";
    }
}
