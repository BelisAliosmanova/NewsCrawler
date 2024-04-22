package com.web.crawler.WebCrawler.repositories;

import com.web.crawler.WebCrawler.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n WHERE n.createdDate = :date")
    List<News> findByCreatedDate(@Param("date") String date);
}
