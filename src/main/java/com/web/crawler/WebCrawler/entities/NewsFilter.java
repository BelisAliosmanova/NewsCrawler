package com.web.crawler.WebCrawler.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class NewsFilter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String newsSite;
    private String filterUrl;

    public NewsFilter() {
    }

    public NewsFilter(Long id, String newsSite, String filterUrl) {
        this.id = id;
        this.newsSite = newsSite;
        this.filterUrl = filterUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewsSite() {
        return newsSite;
    }

    public void setNewsSite(String newsSite) {
        this.newsSite = newsSite;
    }

    public String getFilterUrl() {
        return filterUrl;
    }

    public void setFilterUrl(String filterUrl) {
        this.filterUrl = filterUrl;
    }
}
