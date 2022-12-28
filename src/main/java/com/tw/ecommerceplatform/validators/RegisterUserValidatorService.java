package com.tw.ecommerceplatform.validators;

import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.models.RegisterUserModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class RegisterUserValidatorService implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object registerUserModel, Errors errors) {
        RegisterUserModel model = (RegisterUserModel) registerUserModel;

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

        boolean isValidEmail = model.getUsername().matches(emailRegexPattern);
        boolean isValidPassword = model.getPassword().matches(passwordRegexPattern);
        boolean arePasswordTheSame = model.getPassword().equals(model.getConfirmPassword());

        if (!isValidEmail)
            errors.rejectValue("username", "form.isValidEmail");
        if (!isValidPassword)
            errors.rejectValue("password", "form.isValidPassword");
        if (!arePasswordTheSame)
            errors.rejectValue("confirmPassword", "form.isPasswordTheSame");
    }
}
