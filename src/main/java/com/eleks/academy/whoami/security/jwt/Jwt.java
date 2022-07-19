package com.eleks.academy.whoami.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class Jwt {


  @Value("${jwt.token.secret}")
  private String jwtSecret;

  @Value("${jwt.access-token.expiration}")
  private long jwtExpiration;


  public String generateJwtToken(UserDetails userPrincipal) {
    return generateTokenFromUsername(userPrincipal.getUsername());
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
        .setExpiration(Date.from(Instant.now().plus(jwtExpiration, ChronoUnit.MILLIS))).signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public String getEmailFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) throws JwtException {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
  }
}
