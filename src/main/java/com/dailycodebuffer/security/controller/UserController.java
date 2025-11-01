package com.dailycodebuffer.security.controller;

import com.dailycodebuffer.security.entity.JwtResponse;
import com.dailycodebuffer.security.entity.RefreshToken;
import com.dailycodebuffer.security.entity.RefreshTokenResponse;
import com.dailycodebuffer.security.entity.User;
import com.dailycodebuffer.security.repository.UserRepository;
import com.dailycodebuffer.security.service.JwtService;
import com.dailycodebuffer.security.service.RefreshTokenService;
import com.dailycodebuffer.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    private JwtService jwtService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user)
    {
        return userService.register(user);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody User user)
    {
        return userService.verify(user);
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenResponse refreshTokenRequest)
    {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();
                }).orElseThrow(()-> new RuntimeException(
                        "Refresh Token is not in DB!!!"
                ));
    }

}
