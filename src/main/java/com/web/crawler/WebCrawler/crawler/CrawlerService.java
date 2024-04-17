package com.web.crawler.WebCrawler.crawler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class CrawlerService {
    private final DnevnikBgCrawlerService dnevnikBgCrawlerService;

    public CrawlerService(DnevnikBgCrawlerService dnevnikBgCrawlerService) {
        this.dnevnikBgCrawlerService = dnevnikBgCrawlerService;
    }

    @Scheduled(cron = "* 23 * * * *") //Every hour
    public void crawl() {
        crawlDnevnikBgCrawlerService();
    }

    private void crawlDnevnikBgCrawlerService() {
        try {
            dnevnikBgCrawlerService.crawl();
        } catch (Exception e) {
            System.out.println("Cannot crawl dnevnik");
        }
    }
}
