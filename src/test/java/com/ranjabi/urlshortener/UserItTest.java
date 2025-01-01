package com.ranjabi.urlshortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.ranjabi.urlshortener.user.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserItTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private final String validBearerToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzM1NzM1ODU2LCJleHAiOjE3MzYwOTU4NTZ9.f2TgDNAkRpeC67errHPxgbwJ4R0LjJ-l4y3_rUL-HE4";

    public static <T> int getSize(Iterable<T> iterable) {
        int size = 0;
        for (@SuppressWarnings("unused") T element : iterable) {
            size++;
        }
        return size;
    }

    @Test
    public void fillUserTest() {
        var users = userRepository.findAll();
        var size = getSize(users);
        assertEquals(size, 1);
    }

    @Test
    public void registerTest_ok() throws Exception {
        String requestBody = """
                {
                    "username": "user2",
                    "password": "pass2"
                }
                """;

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Account has been created"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    public void registerTest_usernameAlreadyExists() throws Exception {
        String requestBody = """
                {
                    "username": "user",
                    "password": "pass"
                }
                """;

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Username already exists"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    public void loginTest_ok() throws Exception {
        String requestBody = """
                {
                    "username": "user",
                    "password": "pass"
                }
                """;

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.id").value(1));
    }

    @Test
    public void loginTest_wrongCredential() throws Exception {
        String requestBody = """
                {
                    "username": "user",
                    "password": "pass2"
                }
                """;

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Username/password is wrong"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
