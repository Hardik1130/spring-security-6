package com.dailycodebuffer.security.service;

import com.dailycodebuffer.security.entity.JwtResponse;
import com.dailycodebuffer.security.entity.RefreshToken;
import com.dailycodebuffer.security.entity.User;
import com.dailycodebuffer.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User register(User user)
    {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public JwtResponse verify(User user) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        user.getUserName(),user.getPassword()
//                )
//        );
//
//        if (authentication.isAuthenticated()) {
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUserName());
//            return JwtResponse.builder()
//                    .accessToken(jwtService.generateToken(user))
//                    .token(refreshToken.getToken())
//                    .build();
//        }
//
//        return "failure";


        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUserName(), user.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUserName());
                return JwtResponse.builder()
                        .accessToken(jwtService.generateToken(user))
                        .token(refreshToken.getToken())
                        .build();
            } else {
                throw new RuntimeException("User not authenticated");
            }

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

    }
}
