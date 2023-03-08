package com.mka.springsecurity2v.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

//класс для управления токеном
@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.header}")
    private String authorizationHeader;
    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public JwtTokenProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //шифруем секретный ключ (согласно доке)
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds * 1000);

        //собираем токен
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)  //когда токен создан
                .setExpiration(validity)  //сколько живёт
                .signWith(SignatureAlgorithm.HS256, secretKey)  //алгоритм кодирования и секретный ключ
                .compact();  //сбираем
    }

    //валидация токена по времени его жизни
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());  //до какого-то времени токен не истёк
        } catch (JwtException | IllegalArgumentException exception) {
            throw new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    //получаем сущность Authentication для юзера, чтобы внести его в контекст security
    public Authentication getAuthentication(String token) {
        //получаем Authentication из UserDetailsService
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //читаем из заголовка Authorization запроса токен, если он имеется
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }
}
