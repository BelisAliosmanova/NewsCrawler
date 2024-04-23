package com.web.crawler.WebCrawler.service;

import com.web.crawler.WebCrawler.crawler.DnekvikBg.DnevnikBgCrawler;
import com.web.crawler.WebCrawler.entities.News;
import com.web.crawler.WebCrawler.entities.NewsFilter;
import com.web.crawler.WebCrawler.repositories.NewsRepository;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class DnevnikBgCrawlerTest {

    @Mock
    private NewsFilter newsFilter;

    @Mock
    private NewsRepository newsRepository;

    private DnevnikBgCrawler dnevnikBgCrawler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dnevnikBgCrawler = new DnevnikBgCrawler(newsFilter, newsRepository);
    }

    @Test
    void shouldVisit_ReturnsFalse_ForNonMatchingURL() {
        Page referringPage = mock(Page.class);
        WebURL url = new WebURL();
        url.setURL("https://www.dnevnik.bg/2022/04/20/1234_example_news");
        when(referringPage.getWebURL()).thenReturn(url);

        boolean result = dnevnikBgCrawler.shouldVisit(referringPage, url);

        assertFalse(result);
    }

    @Test
    void visit_DoesNotSaveNews_WhenInvalidHtmlParseData() {
        Page page = mock(Page.class);
        when(page.getParseData()).thenReturn(null);

        dnevnikBgCrawler.visit(page);

        verify(newsRepository, never()).save(any(News.class));
    }

    @Test
    void extractCategoryName_ReturnsCorrectCategory() throws URISyntaxException {
        String url = "https://www.dnevnik.bg/2022/04/21/1234_example_news";

        String category = dnevnikBgCrawler.extractCategoryName(url);

        assertEquals("2022", category);
    }

    @Test
    void extractCategoryName_ReturnsRemainingURL_WhenNoCategory() throws URISyntaxException {
        String url = "https://www.dnevnik.bg/";

        String category = dnevnikBgCrawler.extractCategoryName(url);

        assertEquals("", category);
    }

    @Test
    void extractHeading_ReturnsCorrectHeading() {
        String html = "<html><head></head><body><h1 itemprop=\"name headline\">Test Heading</h1></body></html>";

        String heading = dnevnikBgCrawler.extractHeading(html);

        assertEquals("Test Heading", heading);
    }
}

