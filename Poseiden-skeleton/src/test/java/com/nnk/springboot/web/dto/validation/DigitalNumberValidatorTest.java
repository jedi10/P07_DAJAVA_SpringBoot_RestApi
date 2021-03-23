package com.nnk.springboot.web.dto.validation;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@SpringBootTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DigitalNumberValidatorTest {

    DigitalNumberValidator digitalNumberValidator;

    @Mock
    ConstraintValidatorContext constraintValidatorContext;


    @BeforeEach
    void setUp() {
        digitalNumberValidator = new DigitalNumberValidator();
        //doCallRealMethod().when(digitalNumberValidator).initialize(any());
        //when(digitalNumberValidator.isValid(any(), any())).thenCallRealMethod();

        DigitalNumberValidatorTestClass testClass = new DigitalNumberValidatorTestClass();

        digitalNumberValidator.initialize(testClass);
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("is Digital Number")
    @Order(1)
    @Test
    void isValid() {
        //Given
        String number = "2";

        //When
        boolean result1 = digitalNumberValidator.isValid(number, constraintValidatorContext);

        //Then
        assertTrue(result1);
    }

    @DisplayName("is not Digital Number")
    @Order(2)
    @Test
    void isValid_falseCase() {
        //Given
        String string = "two";

        //When
        boolean result = digitalNumberValidator.isValid(string, constraintValidatorContext);

        //Then
        assertFalse(result);
    }

    @DisplayName("is null")
    @Order(3)
    @Test
    void isValid_nullCase() {
        //Given
        String string = null;

        //When
        boolean result = digitalNumberValidator.isValid(string, constraintValidatorContext);

        //Then
        assertFalse(result);
    }

    private static class DigitalNumberValidatorTestClass implements DigitalNumber {

        @Override
        public String message() {
            return "Test Message";
        }

        @Override
        public Class<?>[] groups() {
            return new Class[]{};
        }

        @Override
        public Class<? extends Payload>[] payload() {
            return new Class[]{};
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return DigitalNumber.class;
        }

    }
}


//https://stackoverflow.com/questions/28768577/how-to-test-a-validator-which-implements-constraintvalidator-in-java
//https://stackoverflow.com/questions/62984669/junit-test-cases-for-javax-custom-validator