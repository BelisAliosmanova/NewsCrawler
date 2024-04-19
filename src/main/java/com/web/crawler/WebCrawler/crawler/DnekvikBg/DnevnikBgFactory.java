package com.web.crawler.WebCrawler.crawler.DnekvikBg;

import com.web.crawler.WebCrawler.entities.NewsFilter;
import com.web.crawler.WebCrawler.repositories.NewsRepository;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;


public class DnevnikBgFactory implements CrawlController.WebCrawlerFactory<WebCrawler> {
    private final NewsFilter newsFilter;
    private final NewsRepository newsRepository;

    public DnevnikBgFactory(NewsFilter newsFilter, NewsRepository newsRepository) {
        this.newsFilter = newsFilter;
        this.newsRepository = newsRepository;
    }

    @Override
    public DnevnikBgCrawler newInstance() {
        return new DnevnikBgCrawler(newsFilter, newsRepository);
    }
}
