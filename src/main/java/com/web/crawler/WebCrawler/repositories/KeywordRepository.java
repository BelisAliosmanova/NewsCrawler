package com.web.crawler.WebCrawler.repositories;

import com.web.crawler.WebCrawler.entities.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findAllByDeletedFalse();

    Keyword findByNameAndDeletedFalse(String name);
    Optional<Keyword> findByIdAndDeletedFalse(Long id);

    @Query("SELECT k FROM Keyword k WHERE k.userId.id = :userId AND k.deleted = false")
    List<Keyword> findByUserIdAndDeletedFalse(@Param("userId") Long userId);
}