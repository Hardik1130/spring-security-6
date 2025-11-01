package com.dailycodebuffer.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class WelcomeController {

    @GetMapping
    public String welcome() {
        return "Welcome to Spring Security 6";
    }

    @GetMapping("/csrf")
    public CsrfToken getToken(HttpServletRequest request)
    {
        return (CsrfToken) request.getAttribute("_csrf");
    }



}
