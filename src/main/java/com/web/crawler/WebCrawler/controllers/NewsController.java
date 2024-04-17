package com.web.crawler.WebCrawler.controllers;

import com.web.crawler.WebCrawler.entities.News;
import com.web.crawler.WebCrawler.repositories.NewsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/news")
public class NewsController {

    private final NewsRepository newsRepository;

    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @GetMapping
    public String getAllNews(Model model) {
        List<News> news = newsRepository.findAll();
        model.addAttribute("allNews", news);
        return "news/list";
    }
}
