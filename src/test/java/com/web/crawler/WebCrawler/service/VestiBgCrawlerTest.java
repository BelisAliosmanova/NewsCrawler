package com.web.crawler.WebCrawler.service;

import com.web.crawler.WebCrawler.crawler.VestiBg.VestiBgCrawler;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VestiBgCrawlerTest {

    @Test
    void extractArticleTime_ReturnsCorrectTime() {
        String htmlContent = "<div class=\"article-info\"><div class=\"article-time\">24 април 2024, 14:30</div></div>";
        Document document = Parser.parse(htmlContent, "https://www.vesti.bg/");

        String articleTime = VestiBgCrawler.extractArticleTime(document.html());

        assertEquals("24 април 2024, 14:30", articleTime);
    }

    @Test
    void dateToLocalDate_ReturnsLocalDateForValidDateString() {
        String date = "24 април 2024";
        LocalDate localDate = VestiBgCrawler.dateToLocalDate(date);

        assertEquals(LocalDate.of(2024, 4, 24), localDate);
    }

    @Test
    void dateToLocalDate_ReturnsNullForInvalidDateString() {
        String date = "invalid date format";
        LocalDate localDate = VestiBgCrawler.dateToLocalDate(date);

        assertNull(localDate);
    }

    @Test
    void extractCategoryName_ReturnsCategoryNameFromUrl() throws URISyntaxException {
        String url = "https://www.vesti.bg/category/article-url";
        String category = VestiBgCrawler.extractCategoryName(url);

        assertEquals("category", category);
    }

    @Test
    void extractCategoryName_ReturnsNullForInvalidUrl() throws URISyntaxException {
        String url = "invalid-url";
        String category = VestiBgCrawler.extractCategoryName(url);

        assertNull(category);
    }
}
