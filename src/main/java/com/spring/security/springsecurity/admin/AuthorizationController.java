package com.spring.security.springsecurity.admin;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

    @GetMapping("/")
    public String index(){
        return "home";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin/pay")
    public String adminPay() {
        return "admin/pay";
    }

    @GetMapping("/admin/**")
    public String admin() {
        return "admin/**";
    }

    @GetMapping("/fail")
    public String fail() {
        return "fail";
    }
}
