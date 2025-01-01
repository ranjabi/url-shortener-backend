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

    @Test
    // TODO: FORMAT RESPONSE
    public void accessProtectedPageWithTokenTest_ok() throws Exception {
        mockMvc.perform(get("/urls")
        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzM1NzI2Mjg2LCJleHAiOjE3MzU3Mjk4ODZ9.bmKHvdZ8SHbYCryTLPJAM2bPO2G4mOdA4TMdLKwscHU"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void accessProtectedPageWithoutTokenTest_unauthorized() throws Exception {
        mockMvc.perform(get("/urls"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Authentication Failed: Missing or Invalid Credentials"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    // TODO accessProtectedPage invalid token
}
