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

    // Тъй като задачата е всеки ден да се теглят новите новини и да се прави статистика
    // паяка се пусна чрез @Scheduled на всеки 24 часа,
    // което ни гарантира, че новините няма да се повтарят.

    // Тъй като извличането на новините от двата сайта и спавенето на статистика изисква време
    // от сървъра и през това време потребителят не може да ползва сайта, докато crawler-ването
    // не приключи, използваме анотацията @Async. Тоест нещата вътре в метода crawl() ще се случват
    // асинхронно, на заден план без да спират дейстрията на други операции.
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
