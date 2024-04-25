package com.web.crawler.WebCrawler.service;

import com.web.crawler.WebCrawler.entities.Keyword;

import java.util.List;

// За да спазим SOLID принципите работим с интерфейси, които после биват имплементирани
public interface KeywordService {
    List<Keyword> getAllKeywordsForUser();

    Keyword getKeywordById(Long id);

    Keyword createKeyword(Keyword Keyword);

    Keyword updateKeyword(Long id, Keyword Keyword);

    void deleteKeyword(Long id);
}
