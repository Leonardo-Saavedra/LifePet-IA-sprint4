package br.com.lifepet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey =
            Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String gerarToken(String email, String perfil) {

        return Jwts.builder()
                .subject(email)
                .claim("perfil", perfil)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60)
                )
                .signWith(secretKey)
                .compact();
    }

    public String getEmail(String token) {

        return extrairClaims(token).getSubject();
    }

    public String getPerfil(String token) {

        return extrairClaims(token).get("perfil", String.class);
    }

    private Claims extrairClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}