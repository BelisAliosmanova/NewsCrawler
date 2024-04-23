package com.web.crawler.WebCrawler.crawler.DnekvikBg;

import com.web.crawler.WebCrawler.entities.NewsFilter;
import com.web.crawler.WebCrawler.repositories.NewsFilterRepository;
import com.web.crawler.WebCrawler.repositories.NewsRepository;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class DnevnikBgCrawlerService {
    private static final String NEWS_SITE = "dnevnik.bg";
    private final NewsFilterRepository newsFilterRepository;

    private final NewsRepository newsRepository;

    public DnevnikBgCrawlerService(NewsFilterRepository newsFilterRepository, NewsRepository newsRepository) {
        this.newsFilterRepository = newsFilterRepository;
        this.newsRepository = newsRepository;
    }

    private static CrawlController getCrawlController(String baseUrl, CrawlConfig config) throws Exception {
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        controller.addSeed(baseUrl);


        return controller;
    }
    public void processCrawl(NewsFilter newsFilter, String baseUrl) throws Exception {
        File crawlStorage = new File("src/test/resources/crawler4j");
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorage.getAbsolutePath());

        int numCrawlers = 1;
        int maxDepthOfCrawling = 2; // Set the maximum depth of crawling
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);

        CrawlController controller = getCrawlController(baseUrl, config);
        controller.start(
                new DnevnikBgFactory(newsFilter, newsRepository), numCrawlers);
    }

    public void crawl() throws Exception {
        List<NewsFilter> newsFilterList = newsFilterRepository.findAllByNewsSite(NEWS_SITE);
        if (!newsFilterList.isEmpty()) {
            for (NewsFilter newsFilter : newsFilterList) {
                String baseUrl = newsFilter.getFilterUrl();
                processCrawl(newsFilter, baseUrl);
            }
        }
    }
}
