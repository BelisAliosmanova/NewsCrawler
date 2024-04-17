package com.web.crawler.WebCrawler.repositories;

import com.web.crawler.WebCrawler.entities.NewsFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsFilterRepository extends JpaRepository<NewsFilter, Long> {
    List<NewsFilter> findAllByNewsSite(String jobSite);
}
