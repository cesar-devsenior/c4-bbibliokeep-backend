package com.devsenior.cdiaz.bibliokeep.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.devsenior.cdiaz.bibliokeep.model.entity.User;
import com.devsenior.cdiaz.bibliokeep.repository.UserRepository;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRegisterUser_WhenValidDataIsProvided() throws Exception {
        var body = """
                {
                    "email": "user@fakeemail.com",
                    "password": "test123",
                    "preferences": [ "darkTheme" ],
                    "annualGoal": 5
                }
                """;

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@fakeemail.com"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());

    }

    @Test
    void shouldRegistrationFailed_WhenInvalidEmailIsProvided() throws Exception {
        var body = """
                {
                    "email": "user",
                    "password": "test123",
                    "preferences": [ "darkTheme" ],
                    "annualGoal": 5
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(detailMatcher("email")));
    }

    @Test
    void shouldLoginSuccess_WhenValidCredentials() throws Exception {
        var user = new User();
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("Password123!"));
        user.setAnnualGoal(10);
        userRepository.save(user);

        var body = """
                {
                    "email": "user@example.com",
                    "password": "Password123!"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void shouldLoginFailed_WhenInvalidCredentials() throws Exception {
        var body = """
                {
                    "email": "user@example.com",
                    "password": "Password123!"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andExpect(status().isUnauthorized());
    }

    private Matcher<String> detailMatcher(String containing) {
        return new BaseMatcher<String>() {
            @Override
            public boolean matches(Object actual) {
                System.out.println("Mensaje: " + actual);
                return actual.toString().contains(containing);
            }

            @Override
            public void describeTo(Description description) {
                // No se necesita una descripción detallada para este matcher
            }
        };
    }

}
