package ru.itmo.finalboss.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itmo.finalboss.entities.UserEntity;
import ru.itmo.finalboss.security.jwt.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {


    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;
    @Mock
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        long userId = 1L;
    }

    @Test
    public void testGenerateToken() {
        Long userId = 1L;
        String token = jwtService.generateToken(userId);

        assertNotNull(token);
    }

    @Test
    public void testExtractUserId() {
        Long userId = 1L;
        String token = jwtService.generateToken(userId);

        String extractedUserId = jwtService.extractUserId(token);

        assertEquals(userId.toString(), extractedUserId);
    }

    @Test
    public void testIsTokenValid() {
        Long userId = 1L;
        String token = jwtService.generateToken(userId);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        boolean isValid = jwtService.isTokenValid(token, userEntity);

        assertTrue(isValid);
    }



    @Test
    public void testIsTokenExpired() {
        String token = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(String.valueOf(1L))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        boolean isExpired = jwtService.isTokenValid(token,userEntity);

        assertTrue(isExpired);
    }

    @Test
    public void testExtractExpiration() {
        Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day from now
        String token = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        Date extractedExpirationDate = jwtService.extractExpiration(token);

        assertFalse(expirationDate ==  extractedExpirationDate);
    }

    @Test
    public void testExtractClaim() {
        String token = Jwts.builder()
                .setSubject("1")
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        Claims claims = jwtService.extractAllClaims(token);

        String subject = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals("1", subject);
    }

    @Test
    public void testGenerateTokenWithExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "USER");

        Long userId = 1L;
        String token = jwtService.generateToken(extraClaims, userId);

        Claims claims = jwtService.extractAllClaims(token);

        assertEquals("USER", claims.get("role"));
        assertEquals(userId.toString(), claims.getSubject());
    }

    @Test
    public void testGetSignInKey() {
        Key key = jwtService.getSignInKey();

        assertNotNull(key);
    }
}
