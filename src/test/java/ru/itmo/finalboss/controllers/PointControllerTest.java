package ru.itmo.finalboss.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.itmo.finalboss.entities.PointEntity;
import ru.itmo.finalboss.entities.UserEntity;
import ru.itmo.finalboss.models.Point;
import ru.itmo.finalboss.models.Role;
import ru.itmo.finalboss.security.jwt.JwtService;
import ru.itmo.finalboss.security.jwt.MyUserDetailsService;
import ru.itmo.finalboss.services.PointService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
public class PointControllerTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MyUserDetailsService myUserDetailsService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SECRET_KEY = "e1430dfb5c82fe52706eb4bc7fb19179bf73323e05abe5ceff1a31291d311db3";

    public String generateToken(Map<String, Object> extraClaims, Long userId) {
        return Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(String.valueOf(1L))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    @Test
    public void testAddPoint() throws Exception {
        String jwtToken = "Bearer " + generateToken(new HashMap<>(), 1L);
        PointEntity point = new PointEntity();
        point.setId(1L);
        point.setX(3D);
        point.setY(4D);
        point.setR(5D);
        point.setResult(Boolean.FALSE);
        point.setExecutedAt(LocalDateTime.now());
        point.setExecutionTime(40L);
        UserEntity userEntity = UserEntity.builder()
                .username("Ivan")
                .password(passwordEncoder.encode("qweq"))
                .role(Role.ADMIN)
                .build();
        point.setUser(userEntity);
        Point pointOwner = Point.PointEntitytoModel(point);
        pointOwner.setOwner("Ivan");

        Mockito.when(pointService.addPoint(jwtToken, point)).thenReturn(pointOwner);

        mockMvc.perform(post("/add")
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pointOwner)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAddPoint_Exception() throws Exception {
        PointEntity point = new PointEntity();
        String jwtToken = "Bearer mockJwtToken";

        Mockito.when(pointService.addPoint(anyString(), Mockito.any(PointEntity.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/add")
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(point)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetPoints() throws Exception {
        Mockito.when(pointService.getPoints()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/points/allPoints"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testGetPoints_Exception() throws Exception {
        Mockito.when(pointService.getPoints()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/points/allPoints"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetUsersPoints() throws Exception {
        String jwtToken = "Bearer mockJwtToken";

        Mockito.when(pointService.getUserPoints(anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/points/getMyPoints")
                        .header("Authorization", jwtToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetUsersPoints_Exception() throws Exception {
        String jwtToken = "Bearer mockJwtToken";

        Mockito.when(pointService.getUserPoints(anyString())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/points/getMyPoints")
                        .header("Authorization", jwtToken))
                .andExpect(status().isUnauthorized());
    }
}
