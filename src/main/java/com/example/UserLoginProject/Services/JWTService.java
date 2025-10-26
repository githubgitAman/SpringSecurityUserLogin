package com.example.UserLoginProject.Services;

import com.example.UserLoginProject.Models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {
    private final long expirationTime = 1000 * 60 * 60 * 24 * 7;
    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String userName){

        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(getKey())
                .compact();
    }
    public Date extractExpiration(String token) {
        return Jwts.parser()                      // ✅ new-style parser
                .verifyWith((SecretKey) getKey())             // verify signature using your secret key
                .build()
                .parseSignedClaims(token)         // parse the JWT
                .getPayload()                     // get Claims payload
                .getExpiration();                 // extract 'exp'
    }
    public boolean validateToken(String token, String username) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenUsername = claims.getSubject();
            Date expiration = claims.getExpiration();

            return (tokenUsername.equals(username) && expiration.after(new Date()));
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
            return false;
        } catch (JwtException e) {
            System.out.println("Invalid token");
            return false;
        }
    }
    public Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
