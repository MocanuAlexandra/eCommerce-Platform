package com.tw.ecommerceplatform.validators;

import com.tw.ecommerceplatform.models.LoginUserModel;
import com.tw.ecommerceplatform.entities.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class LoginValidatorService implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object userEntity, Errors errors) {
        LoginUserModel user = (LoginUserModel) userEntity;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "user.isFieldEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.isFieldEmpty");

        /* Valid email regex pattern - https://owasp.org/www-community/OWASP_Validation_Regex_Repository */
        /* Typical email format: email@domain.com */
        String emailRegexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        boolean isValidEmail =  user.getUsername().matches(emailRegexPattern);

        if(!isValidEmail)
            errors.rejectValue("username", "user.isValidEmail");
    }
}
