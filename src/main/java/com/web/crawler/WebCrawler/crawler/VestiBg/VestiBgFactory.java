package com.web.crawler.WebCrawler.crawler.VestiBg;

import com.web.crawler.WebCrawler.entities.NewsFilter;
import com.web.crawler.WebCrawler.repositories.NewsRepository;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;


// Създава нова инстанция на VestiBgCrawler
public class VestiBgFactory implements CrawlController.WebCrawlerFactory<WebCrawler> {
    private final NewsFilter newsFilter;
    private final NewsRepository newsRepository;

    public VestiBgFactory(NewsFilter newsFilter, NewsRepository newsRepository) {
        this.newsFilter = newsFilter;
        this.newsRepository = newsRepository;
    }

    @Override
    public VestiBgCrawler newInstance() {
        return new VestiBgCrawler(newsFilter, newsRepository);
    }
}
