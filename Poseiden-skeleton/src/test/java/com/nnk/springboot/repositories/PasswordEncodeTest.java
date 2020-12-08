package com.nnk.springboot.repositories;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Created by Khang Nguyen (Email: khang.nguyen@banvien.com), Upgraded by Jedi 
 * @version Alpha1.1.0
 * @since Alpha 1.0.0 09/03/2019 11:26 AM
 */
@SpringBootTest
public class PasswordEncodeTest {

    private String rawPassword = "123456";

    @Test
    public void testPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPW = encoder.encode(rawPassword);
        System.out.println("[ "+ encodedPW + " ]");
        assertNotEquals(rawPassword, encodedPW);
        assertTrue(encoder.matches(rawPassword, encodedPW));
        assertFalse(encoder.matches("123", encodedPW));
    }
}

//https://www.vojtechruzicka.com/stop-using-javadoc-author-tag/
//https://www.yawintutor.com/encode-decode-using-bcryptpasswordencoder-in-spring-boot-security/