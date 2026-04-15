package com.afgicafe.flight.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${spring.security.jwt.secret}")
    private String secret;
    @Value("${spring.security.jwt.expiration}")
    private Long expiration;
    @Value("${spring.security.jwt.issuer}")
    private String issuer;

    private SecretKey getSigningKey () {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername (String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    private Date extractExpiration (String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    private <T> T extractClaim (String jwt, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(jwt));
    }

    public String generateAccessToken (UserDetails userDetails) {
        return generateToken (new HashMap<>(), userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        extraClaims.put(
                "roles",
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            String tokenIssuer = extractClaim(token, Claims::getIssuer);

            return username != null
                    && username.equals(userDetails.getUsername())
                    && issuer.equals(tokenIssuer)
                    && !isTokenExpired(token)
                    && userDetails.isAccountNonExpired()
                    && userDetails.isAccountNonLocked()
                    && userDetails.isCredentialsNonExpired()
                    && userDetails.isEnabled();

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String jwt) {
        try{
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (Exception e) {
            throw  new IllegalArgumentException("Invalid JWT token");
        }
    }
}
