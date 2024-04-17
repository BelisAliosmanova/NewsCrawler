package com.web.crawler.WebCrawler.repositories;

import com.web.crawler.WebCrawler.entities.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findAll();
}
