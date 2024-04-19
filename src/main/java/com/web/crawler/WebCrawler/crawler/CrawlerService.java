package com.web.crawler.WebCrawler.crawler;

import com.web.crawler.WebCrawler.crawler.DnekvikBg.DnevnikBgCrawlerService;
import com.web.crawler.WebCrawler.crawler.VestiBg.VestiBgCrawlerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class CrawlerService {
    private final DnevnikBgCrawlerService dnevnikBgCrawlerService;
    private final VestiBgCrawlerService vestiBgCrawlerService;

    public CrawlerService(DnevnikBgCrawlerService dnevnikBgCrawlerService, VestiBgCrawlerService vestiBgCrawlerService) {
        this.dnevnikBgCrawlerService = dnevnikBgCrawlerService;
        this.vestiBgCrawlerService = vestiBgCrawlerService;
    }

    @Scheduled(cron = "* 34 * * * *") //Every hour
    @Async
    public void crawl() {
        crawlDnevnikBgCrawlerService();
        crawlVestiBgCrawlerService();
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
