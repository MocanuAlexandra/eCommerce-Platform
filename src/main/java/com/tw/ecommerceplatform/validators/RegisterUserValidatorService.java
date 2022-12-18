package com.tw.ecommerceplatform.validators;

import com.tw.ecommerceplatform.models.RegisterUserModel;
import com.tw.ecommerceplatform.entities.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class RegisterUserValidatorService implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object userEntity, Errors errors) {
        RegisterUserModel user = (RegisterUserModel) userEntity;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "user.isEmailEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.isPasswordEmpty");

        /* Valid email regex pattern - https://owasp.org/www-community/OWASP_Validation_Regex_Repository */
        /* Typical email format: email@domain.com */
        String emailRegexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        /*
           at least 8 digits {8,}
           at least one number (?=.*\d)
           at least one lowercase (?=.*[a-z])
           at least one uppercase (?=.*[A-Z])
           at least one special character (?=.*[@#$%^&+=])
           No space [^\s]
        */
        String passwordRegexPattern = "^(?:(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*)[^\s]{8,}$";

        boolean isValidEmail = user.getUsername().matches(emailRegexPattern);
        boolean isValidPassword = user.getPassword().matches(passwordRegexPattern);
        boolean arePasswordTheSame = user.getPassword().equals(user.getConfirmPassword());

        if (!isValidEmail)
            errors.rejectValue("username", "user.isValidEmail");
        if (!isValidPassword)
            errors.rejectValue("password", "user.isValidPassword");
        if (!arePasswordTheSame)
            errors.rejectValue("confirmPassword", "user.isPasswordTheSame");
    }
}
