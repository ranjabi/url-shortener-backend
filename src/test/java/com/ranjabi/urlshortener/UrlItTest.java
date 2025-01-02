package com.ranjabi.urlshortener;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UrlItTest {

    @Autowired
    private MockMvc mockMvc;

    private final String validBearerToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzM1NzM1ODU2LCJleHAiOjE3MzYwOTU4NTZ9.f2TgDNAkRpeC67errHPxgbwJ4R0LjJ-l4y3_rUL-HE4";

    @Test
    public void createUrl_ok() throws Exception {
        mockMvc.perform(post("/urls?path=https://www.google.com")
                .header("Authorization",
                validBearerToken))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Url has been created"))
                .andExpect(jsonPath("$.data.originalUrl").value("https://www.google.com"));;
    }

    @Test
    public void redirectToOriginalUrlTest_ok() throws Exception {
        mockMvc.perform(get("/urls/ok")
                .header("Authorization",
                        validBearerToken))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void redirectToOriginalUrlTest_notFound() throws Exception {
        mockMvc.perform(get("/urls/null")
                .header("Authorization",
                validBearerToken))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Url not found"))
                .andExpect(jsonPath("$.data").value(nullValue()));;
    }
}
