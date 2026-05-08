package com.devsenior.cdiaz.bibliokeep.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import com.devsenior.cdiaz.bibliokeep.config.security.JwtTokenProvider;
import com.devsenior.cdiaz.bibliokeep.config.security.UserPrincipal;
import com.devsenior.cdiaz.bibliokeep.model.entity.Book;
import com.devsenior.cdiaz.bibliokeep.model.entity.BookStatus;
import com.devsenior.cdiaz.bibliokeep.model.entity.User;
import com.devsenior.cdiaz.bibliokeep.repository.BookRepository;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/sql/load-users.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwt;

    @Autowired
    private BookRepository bookRepository;

    private String token;

    @BeforeEach
    void generateToken() {
        var userPrincipal = new UserPrincipal(testUser());
        var authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal,
                null,
                userPrincipal.getAuthorities());

        token = jwt.generateToken(authentication);
    }

    @Test
    void shouldCreateSuccess_WhenValidData() throws Exception {
        var body = """
                {
                    "isbn": "0000000000",
                    "title": "Test Book",
                    "authors": ["Test Author"],
                    "description": "Test Description",
                    "thumbnail": "http://test.org/testimage.png",
                    "status": "COMPRADO",
                    "rating": 5
                }
                """;

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer %s".formatted(token))
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void shouldReturnAllBooks_WhenQueryIsEmpty() throws Exception {

        mockMvc.perform(get("/api/books/search")
                .header("Authorization", "Bearer %s".formatted(token)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    void shouldUpdateBook_WhenValidData() throws Exception {
        // Arrange
        var book = bookRepository.save(Book.builder()
                .isbn("1234567890")
                .title("Test Book")
                .owner(testUser())
                .description("Test Book Description")
                .authors(List.of("Test Author"))
                .rating(5)
                .status(BookStatus.DESEADO)
                .build());

        var body = """
                {
                    "status": "COMPRADO"
                }
                """;

        // Act & Assert
        mockMvc.perform(patch("/api/books/%d/status".formatted(book.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer %s".formatted(token))
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.status").value("COMPRADO"));
    }

    private User testUser() {
        return User.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .email("testuser@test.com")
                .password("testpassword")
                .build();
    }
}
