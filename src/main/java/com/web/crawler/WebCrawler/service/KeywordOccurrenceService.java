package com.web.crawler.WebCrawler.service;

import com.web.crawler.WebCrawler.entities.KeywordOccurrence;

import java.util.List;

// За да спазим SOLID принципите работим с интерфейси, които после биват имплементирани
public interface KeywordOccurrenceService {
    void generateDailyKeywordStatistics();
    List<KeywordOccurrence> allKeywordOccurrencesBuLoggedUser();
}
