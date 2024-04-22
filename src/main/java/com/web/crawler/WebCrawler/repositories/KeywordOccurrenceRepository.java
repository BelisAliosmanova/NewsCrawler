package com.web.crawler.WebCrawler.repositories;

import com.web.crawler.WebCrawler.entities.KeywordOccurrence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordOccurrenceRepository extends JpaRepository<KeywordOccurrence, Long> {
}
