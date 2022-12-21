package com.tw.ecommerceplatform.validators;

import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.models.CreateItemModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class CreateItemValidatorService implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object createItemModel, Errors errors) {
        CreateItemModel model = (CreateItemModel) createItemModel;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "form.isFieldEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", "form.isFieldEmpty");

        /*
           The quantity must be a positive integer
        */
        String quantityRegexPattern = "^[+]?\\d+$";
        String quantity = Integer.toString(model.getQuantity());

        boolean isValidQuantity =quantity.matches(quantityRegexPattern);

        if (!isValidQuantity)
            errors.rejectValue("quantity", "form.isValidQuantity");
    }
}

