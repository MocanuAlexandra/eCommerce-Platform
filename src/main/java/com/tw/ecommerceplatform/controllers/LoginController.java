package com.tw.ecommerceplatform.controllers;

import com.tw.ecommerceplatform.models.LoginUserModel;
import com.tw.ecommerceplatform.validators.LoginValidatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//TODO: make error messages visible in the login page
@Controller
public class LoginController {
    LoginValidatorService loginValidatorService;

    @GetMapping("/login")
    public String open(Model model) {
        model.addAttribute("form", new LoginUserModel());
        return "login";
    }

    @PostMapping("/login")
    public String login( @ModelAttribute("form") LoginUserModel form,
                        BindingResult bindingResult) {

        // Validate the form
        loginValidatorService.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Redirect to the default page
        return "user";
    }
}
