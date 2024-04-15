package com.web.crawler.WebCrawler.constants;

public enum Role {
    ADMIN("ADMIN", 1),
    USER("USER", 2);

    Role(String label, int accessLevel){
        this.label = label;
    }

    private final String label;

    @Override
    public String toString() {
        return this.label;
    }
}
