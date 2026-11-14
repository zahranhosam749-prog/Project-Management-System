
package com.example.projectmanagement.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                java.util.Base64.getDecoder().decode(secret.trim())
        );
    }

    public String generateToken(String email) {

        long expirationTime = 86400000;
        String token = Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();


        return token;
    }

    public String extractUsername(String token) {
        System.out.println("JWT Secret Used: [" + secret.trim() + "]");

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public String getTokenFingerprint(String token) {
        try {
            byte[] hash = java.security.MessageDigest
                    .getInstance("SHA-256")
                    .digest(token.getBytes(java.nio.charset.StandardCharsets.US_ASCII));

            return java.util.HexFormat.of().formatHex(hash);

        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

}
