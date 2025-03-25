package com.even.gestion.controllers;

import com.even.gestion.models.AppUser;
import com.even.gestion.models.RegisterDto;
import com.even.gestion.repositories.AppUserRepository;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class AccountController {
    @Autowired
    private AppUserRepository repo;


    @GetMapping("register")
    public String register(Model model) {
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute(registerDto);
        model.addAttribute("success", false);
        return "register";
    }
    @PostMapping("/register")
    public String register(Model model,@Valid @ModelAttribute RegisterDto registerDto,
                           BindingResult result) {
        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())){
            result.addError(
                    new FieldError("registerDto","confirmPassword", "password and Confirm Password do not match")
            );}
        AppUser appUser = repo.findByEmail(registerDto.getEmail());
        if (appUser != null) {
            result.addError(
                    new FieldError("registerDto", "email",
                          "Email address is already used")
                          );
            }
        if (result.hasErrors()){
            return "register";
        }
        try {
        var  bcryptEncoder = new BCryptPasswordEncoder();

        AppUser newUser = new AppUser();
        newUser.setLastName(registerDto.getLastName());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPhone(registerDto.getPhone());
        newUser.setAddress(registerDto.getAddress());
        newUser.setRole("client");
        newUser.setCreatedAt(new Date());
        newUser.setPassword(bcryptEncoder.encode(registerDto.getPassword()));

        repo.save(newUser);
        model.addAttribute("register", new RegisterDto());
        model.addAttribute("success", true);
        }
        catch(Exception ex){
            result.addError(
                    new FieldError("registerDto", "firstName",
                            ex.getMessage())
            );
        }

        // Add logic for handling the registration
        return "register";
    }
    @Controller
    public class LoginController {
        @GetMapping("/login")
        public String login() {
            return "login"; // Your custom login.html
        }
    }


}
