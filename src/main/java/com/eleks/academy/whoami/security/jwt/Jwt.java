package com.eleks.academy.whoami.security.jwt;

import com.eleks.academy.whoami.security.exception.NotAcceptableOauthException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class Jwt {

    @Value("${jwt.token-secret}")
    private String jwtSecret;


    public String generateToken(String subject, Long expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(expiration, ChronoUnit.MILLIS))).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new NotAcceptableOauthException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            throw new NotAcceptableOauthException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new NotAcceptableOauthException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new NotAcceptableOauthException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new NotAcceptableOauthException("JWT claims string is empty");
        }
        return true;
    }
}
