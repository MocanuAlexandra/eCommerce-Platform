package com.tw.ecommerceplatform.validators;

import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.models.ChangePasswordUserModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class ChangePasswordValidatorService implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object changePasswordModel, Errors errors) {
        ChangePasswordUserModel model = (ChangePasswordUserModel) changePasswordModel;

        /*
           at least 8 digits {8,}
           at least one number (?=.*\d)
           at least one lowercase (?=.*[a-z])
           at least one uppercase (?=.*[A-Z])
           at least one special character (?=.*[@#$%^&+=])
           No space [^\s]
        */
        String passwordRegexPattern = "^(?:(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*)[^\s]{8,}$";

        boolean isValidPassword = model.getNewPassword().matches(passwordRegexPattern);
        boolean arePasswordTheSame = model.getNewPassword().equals(model.getConfirmPassword());

        if (!isValidPassword)
            errors.rejectValue("newPassword", "form.isValidPassword");
        if (!arePasswordTheSame)
            errors.rejectValue("confirmPassword", "form.isPasswordTheSame");
    }
}

