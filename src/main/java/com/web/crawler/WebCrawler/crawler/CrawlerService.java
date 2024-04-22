package com.web.crawler.WebCrawler.crawler;

import com.web.crawler.WebCrawler.crawler.DnekvikBg.DnevnikBgCrawlerService;
import com.web.crawler.WebCrawler.crawler.VestiBg.VestiBgCrawlerService;
import com.web.crawler.WebCrawler.service.KeywordOccurrenceService;
import com.web.crawler.WebCrawler.service.KeywordService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class CrawlerService {
    private final DnevnikBgCrawlerService dnevnikBgCrawlerService;
    private final VestiBgCrawlerService vestiBgCrawlerService;
    private final KeywordOccurrenceService keywordOccurrenceService;

    public CrawlerService(DnevnikBgCrawlerService dnevnikBgCrawlerService, VestiBgCrawlerService vestiBgCrawlerService, KeywordOccurrenceService keywordOccurrenceService) {
        this.dnevnikBgCrawlerService = dnevnikBgCrawlerService;
        this.vestiBgCrawlerService = vestiBgCrawlerService;
        this.keywordOccurrenceService = keywordOccurrenceService;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Every 24 hours
    @Async
    public void crawl() {
        crawlDnevnikBgCrawlerService();
        crawlVestiBgCrawlerService();
        keywordOccurrenceService.generateDailyKeywordStatistics();
        System.out.println("STOP");
    }

    private void crawlVestiBgCrawlerService() {
        try {
            vestiBgCrawlerService.crawl();
        } catch (Exception e) {
            System.out.println("Cannot crawl vesti");
        }
    }
    private void crawlDnevnikBgCrawlerService() {
        try {
            dnevnikBgCrawlerService.crawl();
        } catch (Exception e) {
            System.out.println("Cannot crawl dnevnik");
        }
    }
}
