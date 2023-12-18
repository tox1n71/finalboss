package ru.itmo.finalboss;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.itmo.finalboss.models.UserResponse;
import ru.itmo.finalboss.security.jwt.JwtService;
import ru.itmo.finalboss.security.jwt.MyUserDetailsService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MyUserDetailsService myUserDetailsService;



    @Test
    public void registrationEndPointTest() throws Exception {

        String username = "test899";
        String password = "123";
        MvcResult result = mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println(responseBody);
        UserResponse responseObj = objectMapper.readValue(responseBody, UserResponse.class);
        String token = responseObj.getToken();
        boolean isTokenValid = jwtService.isTokenValid(token, myUserDetailsService.loadUserByUsername(username));
        assertTrue(isTokenValid);
    }

    @Test
    public void loginEndPointTest() throws Exception {
        String username = "test802";
        String password = "123";
        MvcResult result = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println(responseBody);
        UserResponse responseObj = objectMapper.readValue(responseBody, UserResponse.class);
        String token = responseObj.getToken();
        boolean isTokenValid = jwtService.isTokenValid(token, myUserDetailsService.loadUserByUsername(username));
        assertTrue(isTokenValid);
    }

}
