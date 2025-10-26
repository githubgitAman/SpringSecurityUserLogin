package com.example.UserLoginProject.Services;

import com.example.UserLoginProject.Models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String userName){

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                //Expiration for 14 Days
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7 * 2))
                .and()
                .signWith(getKey())
                .compact();
    }
    public Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public Date extractExpiration(String token) {
        return Jwts.parser()                      // ✅ new-style parser
                .verifyWith((SecretKey) getKey())             // verify signature using your secret key
                .build()
                .parseSignedClaims(token)         // parse the JWT
                .getPayload()                     // get Claims payload
                .getExpiration();                 // extract 'exp'
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenUsername = claims.getSubject();
            Date expiration = claims.getExpiration();

            return (tokenUsername.equals(userDetails.getUsername()) && expiration.after(new Date()));
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
            return false;
        } catch (JwtException e) {
            System.out.println("Invalid token");
            return false;
        }
    }
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // "sub" claim
    }
}
