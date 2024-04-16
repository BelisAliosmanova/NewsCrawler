package com.web.crawler.WebCrawler.repositories;

import com.web.crawler.WebCrawler.entities.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findAll();
}
