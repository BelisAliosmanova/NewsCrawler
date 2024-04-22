package com.web.crawler.WebCrawler.entities;

import jakarta.persistence.*;

@Entity
public class KeywordOccurrence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private Keyword keywordId;

    private int dailyOccurrences;

    private String date;

    public KeywordOccurrence() {
    }

    public KeywordOccurrence(Long id, Keyword keywordId, int dailyOccurrences, String date) {
        this.id = id;
        this.keywordId = keywordId;
        this.dailyOccurrences = dailyOccurrences;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Keyword getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Keyword keywordId) {
        this.keywordId = keywordId;
    }

    public int getDailyOccurrences() {
        return dailyOccurrences;
    }

    public void setDailyOccurrences(int dailyOccurrences) {
        this.dailyOccurrences = dailyOccurrences;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
