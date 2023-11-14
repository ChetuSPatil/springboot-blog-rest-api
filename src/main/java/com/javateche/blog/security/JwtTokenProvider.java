package com.javateche.blog.security;

import com.javateche.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app-jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    //Generate JWT token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();

        Date expiryDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String jwtToken = Jwts.builder()
                                .subject(username)
                                .issuedAt(new Date())
                                .expiration(expiryDate)
                                .signWith(key())
                                .compact();

        return jwtToken;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //Get username from JWT token
    public String getUsername(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        return username;
    }

    //Validate JWT token
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parse(token);

            return true;
        } catch (MalformedJwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }
    }
}
