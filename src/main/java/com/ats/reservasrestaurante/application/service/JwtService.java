package com.ats.reservasrestaurante.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public record JwtService(
  @Value("${com.ats.reservasrestaurante.jwtSecret}")
  String secretKey,

  @Value("${com.ats.reservasrestaurante.expirationMs}")
  Long jwtExpiration
) {

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSignInKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private LocalDateTime extractExpiration(String token) {
    Date date = extractClaim(token, Claims::getExpiration);
    return date.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDateTime();
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).isBefore(LocalDateTime.now());
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUserName(token);
    return (username.equals(userDetails.getUsername()))
      && (!isTokenExpired(token));
  }

  private String buildToken(
    HashMap<String, Object> extraClaims,
    UserDetails userDetails,
    Long expiration
  ) {
    return Jwts.builder()
      .setClaims(extraClaims)
      .setSubject(userDetails.getUsername())
      .setIssuedAt(
        Date.from(
          LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .toInstant()
        )
      )
      .setExpiration(
        Date.from(
          LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .plusMillis(expiration)
        )
      )
      .signWith(getSignInKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public String generateToken(UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, jwtExpiration);
  }

  public String generateToken(HashMap<String, Object> extraClaims, UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  /*
  Expresion -> resultado
  2x^2 + 4x - 8 = 0
  () -> logic
  (a, b, c) -> a+b-c
  (claims) -> claims.getSubject
  (claims) -> claims.getExpiration
  (claims) -> claims.getId
  Function<Claims, T>
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

}
