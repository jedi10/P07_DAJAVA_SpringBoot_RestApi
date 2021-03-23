package com.nnk.springboot.web.dto.validation;

import com.nnk.springboot.web.dto.UserDTO;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.*;
import java.lang.annotation.Annotation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@SpringBootTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PasswordConstraintValidatorTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        userDTO = new UserDTO(1,"Joe", null, "Mr Joe Mirror", "USER");
    }

    @AfterEach
    void tearDown() {
        validatorFactory.close();
    }


    @DisplayName("password valid")
    @Order(1)
    @Test
    void isValid_passwordOk() {
        //given
        userDTO.setPassword("Mypass2^");

        //When
        Set<ConstraintViolation<UserDTO>> violations
                = validator.validate(userDTO);

        //Then
        assertTrue(violations.isEmpty());
    }

    @DisplayName("short password invalid")
    @Order(2)
    @Test
    void isValid_shortPassword() {
        //given
        userDTO.setPassword("Mpass2^");

        //When
        Set<ConstraintViolation<UserDTO>> violations
                = validator.validate(userDTO);

        //Then
        assertFalse(violations.isEmpty());
        ConstraintViolation<UserDTO> violation = violations.stream()
                .findFirst().get();
        assertEquals("password", violation.getPropertyPath().toString());
        assertTrue(violation.getMessageTemplate().contains("Password must be 8"));
    }

    @DisplayName("null password invalid")
    @Order(3)
    @Test
    void isValid_null() {
        //given
        userDTO.setPassword(null);

        //When
        Set<ConstraintViolation<UserDTO>> violations
                = validator.validate(userDTO);

        //Then
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        ConstraintViolation<UserDTO> violation = violations.stream()
                .filter(v-> v.getMessageTemplate().equals("password can't be null"))
                .findFirst().get();//findAny().orElse(null)
        assertNotNull(violation);
        assertEquals("password", violation.getPropertyPath().toString());
        assertTrue(violation.getMessageTemplate().contains("password can't be null"));
    }
}


//https://farenda.com/java/bean-validation-unit-testing/
//https://stackoverflow.com/questions/15409763/how-to-obtain-constraintvalidatorcontext


//iterate a set https://stackoverflow.com/questions/7283338/getting-an-element-from-a-set