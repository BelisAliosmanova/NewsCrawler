package com.web.crawler.WebCrawler.controllers;

import com.web.crawler.WebCrawler.constants.Role;
import com.web.crawler.WebCrawler.entities.User;
import com.web.crawler.WebCrawler.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user = userRepository.findByUsername(username);
        if (user == null || passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "User not found.");
            return "login";
        }
        return "redirect:/chat";
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // След регистрация всеки user е с роля USER
        userRepository.save(user);
        return "redirect:/login";
    }
}

