package com.nnk.springboot.domain;

import com.nnk.springboot.web.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {

    @Test
    void testHashCode() {
        //GIVEN
        User userGiven1 = new User("toto", "XXX", "Aspic", "USER");
        int hashCodeGiven = Objects.hash(userGiven1.getUsername(), userGiven1.getFullname(), userGiven1.getRole());

        //When
        int hashCodeResult = userGiven1.hashCode();

        //THEN
        assertEquals(hashCodeGiven, hashCodeResult);

    }
}