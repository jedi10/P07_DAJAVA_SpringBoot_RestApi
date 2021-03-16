package com.nnk.springboot.web.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DigitalNumberValidator implements ConstraintValidator<DigitalNumber, String> {

    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    public boolean isValid(String strNum, ConstraintValidatorContext constraintValidatorContext) {
        if (strNum == null) {
            return false;
        }
        if (pattern.matcher(strNum).matches()){
            return true;
        }
        //constraintValidatorContext.buildConstraintViolationWithTemplate("Entry is not numerical")
        //        .addConstraintViolation()
        //        .disableDefaultConstraintViolation();
        return false;
    }
}
