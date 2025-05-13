package tani.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtils {

    private final SecretKey secretKey = Keys.hmacShaKeyFor("secretsecretsecretsecretsecretsecretsecretsecret".getBytes());

    public String generarToken(String email, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(1L, ChronoUnit.HOURS)))
                .signWith(secretKey) // algoritmo inferido automáticamente
                .compact();
    }

    public String generarTokenConExpiracion(String correo, Map<String, Object> claims, int minutos) {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(minutos, ChronoUnit.MINUTES);
        return Jwts.builder()
                .claims(claims)
                .subject(correo)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationTime))
                .signWith(secretKey) // algoritmo inferido automáticamente
                .compact();
    }

    public String obtenerCorreoDesdeToken(String token) {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
        return claims.getPayload().getSubject();
    }

    public Jws<Claims> parseJwt(String jwtString) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwtString);
    }
}