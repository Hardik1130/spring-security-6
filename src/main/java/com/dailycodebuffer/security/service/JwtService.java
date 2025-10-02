package com.dailycodebuffer.security.service;

import com.dailycodebuffer.security.entity.User;
import com.fasterxml.jackson.databind.DatabindException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.Table;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Service
public class JwtService {

    private String secretKey = null;

    public String generateToken(User user) {
        Map<String,Object> claims
                = new HashMap<>();
//        return Jwts
//                .builder()
//                .setClaims(claims)
//                .subject(user.getUserName())
//                .issuer("DCB")
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis()+60*10*1000))
//                .and()
//                .signWith(generateKey())
//                .compact();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUserName())
                .setIssuer("DCB")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 10 * 1000))
                .signWith(generateKey())
                .compact();
    }

    public String getSecretKey()
    {
        return secretKey = "b387715dfc9b511a551d75ac1f215faf35180d405ea146e2712fb88485e5cf8e";
    }

    private SecretKey generateKey()
    {
        byte[] decode =
                Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(decode);
    }


    public String extractUserName(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T>T extractClaims(String token, Function<Claims, T> claimResolver)
    {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())   // key to validate signature
                .build()
                .parseClaimsJws(token)          // parses and validates JWT
                .getBody();                     // returns Claims (payload)
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token)
    {
        return  extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token)
    {
        return extractClaims(token, Claims::getExpiration);
    }
}
