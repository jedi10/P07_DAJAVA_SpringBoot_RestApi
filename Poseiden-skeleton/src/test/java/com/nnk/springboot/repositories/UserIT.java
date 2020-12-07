package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserIT {

    @Autowired
    private UserRepository userRepository;

    private User userGiven;

    @BeforeAll
    void setUpAll() {
        userGiven = new User("toto", "XXX", "Aspic", "USER");
    }

    @Order(1)
    @Test
    public void userSaveTest() {
        // Save
        userGiven = userRepository.save(userGiven);
        assertNotNull(userGiven.getId());
        assertEquals("toto", userGiven.getUsername());
    }

    @Order(2)
    @Test
    public void userUpdateTest() {
        // Update
        userGiven.setUsername("snake");
        userGiven = userRepository.save(userGiven);
        assertEquals("snake", userGiven.getUsername());
    }

    @Order(3)
    @Test
    public void userFindTest() {
        // Find
        List<User> listResult = userRepository.findAll();
        assertTrue(listResult.size() > 0);
    }

    @Order(4)
    @Test
    public void userDeleteTest() {
        // Delete
        Integer id = userGiven.getId();
        userRepository.delete(userGiven);
        Optional<User> userList = userRepository.findById(id);
        assertFalse(userList.isPresent());
    }

}
