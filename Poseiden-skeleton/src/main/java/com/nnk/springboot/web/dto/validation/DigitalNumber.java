package com.nnk.springboot.web.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <b>Custom Annotation for Digital Number Validation</b>
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = DigitalNumberValidator.class)
@Documented
public @interface DigitalNumber {

    String message() default "Entry have to be a digital number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
