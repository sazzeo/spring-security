package com.example.chapter1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {


    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/invalid-session")
    public String invalidSession() {
        return "계정이 이미 접속중입니다.";
    }

    @GetMapping("/expired")
    public String expired() {
        return "계정이 만료되었습니다.";
    }
}
