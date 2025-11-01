package com.dailycodebuffer.security.service;

import com.dailycodebuffer.security.entity.RefreshToken;
import com.dailycodebuffer.security.repository.RefreshTokenRepository;
import com.dailycodebuffer.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String username)
    {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUserName(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token)
    {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token)
    {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            System.out.println("The token time : "+token.getExpiryDate());
            System.out.println("The now : "+Instant.now());
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken()+" Refresh token expired");
        }
        return token;
    }

}
