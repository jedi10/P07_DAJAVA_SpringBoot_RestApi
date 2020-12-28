package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "duke", roles = { "USER", "ADMIN"})
class LoginControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private LoginController loginController;

    private static List<User> userListGiven = new ArrayList<>();

    static {
        User userGiven1 = new User("toto", "XXX", "Aspic", "USER");
        User userGiven2 = new User("toto2", "YYY", "Aspic2", "USER");
        userListGiven.add(userGiven1);
        userListGiven.add(userGiven2);
    }

    @BeforeEach
    void setUp() {
        //***********GIVEN*************
        //          Mockito config
        when(userRepository.findAll()).thenReturn(userListGiven);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void login() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Please sign in")));
    }

    @Order(2)
    @Test
    void getAllUserArticles() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/app/secure/article-details"))
                .andExpect(status().isOk())//HttpStatus 200
                .andExpect(view().name("user/list"))
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("User List")))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", userListGiven));
    }

    @Order(3)
    @Test
    void error() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/app/error"))
                .andExpect(status().isOk())//HttpStatus 200
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"))
                .andExpect(content().string(containsString("Access Denied Exception")))
                .andExpect(view().name("403"))
                .andExpect(model().attributeExists("errorMsg"))
                .andExpect(model().attribute("errorMsg", "You are not authorized for the requested data."));
    }
}