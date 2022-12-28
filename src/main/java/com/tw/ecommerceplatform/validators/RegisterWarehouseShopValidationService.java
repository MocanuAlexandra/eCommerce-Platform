package com.tw.ecommerceplatform.validators;

import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.models.RegisterWarehouseShopModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class RegisterWarehouseShopValidationService implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object registerWarehouseShopModel, Errors errors) {
        RegisterWarehouseShopModel model = (RegisterWarehouseShopModel) registerWarehouseShopModel;

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

             "Acme Corporation"
             "Big Corp Inc."
             "Small Business LLC"

        */
        String nameRegexPattern = "^[A-Z][a-zA-Z]+(?: [A-Z][a-zA-Z]+)*$\n";

        /* Code format by 5 digits */
        String codeRegexPattern = "^[0-9]{5}$";

        boolean isValidEmail = model.getUsername().matches(emailRegexPattern);
        boolean isValidPassword = model.getPassword().matches(passwordRegexPattern);
        boolean arePasswordTheSame = model.getPassword().equals(model.getConfirmPassword());
        boolean isNameValid = model.getName().matches(nameRegexPattern);
        boolean isAddressValid = model.getAddress().matches(addressRegexPattern);
        boolean isCodeNumberValid = model.getCode().matches(codeRegexPattern);

        if (!isValidEmail)
            errors.rejectValue("username", "form.isValidEmail");
        if (!isValidPassword)
            errors.rejectValue("password", "form.isValidPassword");
        if (!arePasswordTheSame)
            errors.rejectValue("confirmPassword", "form.isPasswordTheSame");
        if (!isNameValid)
            errors.rejectValue("name", "form.isNameValid");
        if (!isAddressValid)
            errors.rejectValue("address", "form.isAddressValid");
        if (!isCodeNumberValid)
            errors.rejectValue("code", "form.isCodeNumberValid");

    }
}

