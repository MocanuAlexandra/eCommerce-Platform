package com.tw.ecommerceplatform.validators;

import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.models.RegisterWarehouseModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class RegisterWarehouseValidationService implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object userEntity, Errors errors) {
        RegisterWarehouseModel user = (RegisterWarehouseModel) userEntity;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "user.isFieldEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.isFieldEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "user.isFieldEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "user.isFieldEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "user.isFieldEmpty");


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

        /*
        * This regex will match an address with a street name followed by a space, then a street number.
        * For example, the following addresses would be matched by this regex:

            "Main Street 123"
            "Maple Ave 456"

         */
        String addressRegexPattern = "^[A-Z][a-zA-Z]*(\\s[A-Z][a-zA-Z]*)?\\s[0-9]+$";

        /*
        This regex will match a company name with one or more words,
        where each word consists of a capital letter followed by zero or more letters that may be
        lowercase or uppercase, and where an optional second word is also allowed.

        For example, the following company names would be matched by this regex:

             "Firma"
             "Ceva Numa"
             "Alt Nume"
             "Companie Cu Nume Lung"

        */
        String nameRegexPattern = "^[A-Z][a-zA-Z]*(\\s[A-Z][a-zA-Z]*)+$";

        /* Zip code format by 5 digits */
        String zipCodeRegexPattern = "^[0-9]{5}$";

        boolean isValidEmail = user.getUsername().matches(emailRegexPattern);
        boolean isValidPassword = user.getPassword().matches(passwordRegexPattern);
        boolean arePasswordTheSame = user.getPassword().equals(user.getConfirmPassword());
        boolean isNameValid = user.getName().matches(nameRegexPattern);
        boolean isAddressValid = user.getAddress().matches(addressRegexPattern);
        boolean isCodeNumberValid = user.getCode().matches(zipCodeRegexPattern);

        if (!isValidEmail)
            errors.rejectValue("username", "user.isValidEmail");
        if (!isValidPassword)
            errors.rejectValue("password", "user.isValidPassword");
        if (!arePasswordTheSame)
            errors.rejectValue("confirmPassword", "user.isPasswordTheSame");
        if (!isNameValid)
            errors.rejectValue("name", "user.isNameValid");
        if (!isAddressValid)
            errors.rejectValue("address", "user.isAddressValid");
        if (!isCodeNumberValid)
            errors.rejectValue("code", "user.isCodeNumberValid");

    }
}

