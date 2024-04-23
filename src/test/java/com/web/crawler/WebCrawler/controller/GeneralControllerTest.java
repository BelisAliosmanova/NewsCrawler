package com.web.crawler.WebCrawler.controller;

import com.web.crawler.WebCrawler.controllers.GeneralController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GeneralControllerTest {

    @InjectMocks
    private GeneralController generalController;

    @Test
    public void testMenu() {
        String viewName = generalController.menu();

        assertThat(viewName).isEqualTo("home");
    }
}