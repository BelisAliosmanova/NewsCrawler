package com.web.crawler.WebCrawler.repositories;

import com.web.crawler.WebCrawler.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
