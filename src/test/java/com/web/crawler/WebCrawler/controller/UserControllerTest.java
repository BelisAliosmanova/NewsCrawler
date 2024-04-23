package com.web.crawler.WebCrawler.controller;

import com.web.crawler.WebCrawler.constants.Role;
import com.web.crawler.WebCrawler.controllers.UserController;
import com.web.crawler.WebCrawler.entities.User;
import com.web.crawler.WebCrawler.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    @Test
    public void testLoginForm() {
        String viewName = userController.loginForm();

        assertThat(viewName).isEqualTo("login");
    }

    @Test
    public void testLogin_UserNotFound() {
        String username = "testuser";
        String password = "testpassword";
        when(userRepository.findByUsername(username)).thenReturn(null);

        String viewName = userController.login(username, password, model);

        assertThat(viewName).isEqualTo("login");
        verify(model).addAttribute("error", "User not found.");
    }

    @Test
    public void testRegistrationForm() {
        String viewName = userController.registrationForm(model);

        assertThat(viewName).isEqualTo("register");
    }

    @Test
    public void testRegister_Successful() {
        User user = new User();
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedpassword");

        String viewName = userController.register(user, bindingResult);

        assertThat(viewName).isEqualTo("redirect:/login");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        verify(userRepository).save(user);
    }
}
