package com.web.crawler.WebCrawler.crawler.DnekvikBg;

import com.web.crawler.WebCrawler.entities.News;
import com.web.crawler.WebCrawler.entities.NewsFilter;
import com.web.crawler.WebCrawler.repositories.NewsRepository;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DnevnikBgCrawler extends WebCrawler {
    private final NewsFilter newsFilter;

    private final NewsRepository newsRepository;

    private final static String SITE = "https://www.dnevnik.bg/";

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    public DnevnikBgCrawler(NewsFilter newsFilter, NewsRepository newsRepository) {
        this.newsFilter = newsFilter;
        this.newsRepository = newsRepository;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL();
        Pattern pattern = Pattern.compile("\\d{4}/\\d{2}/\\d{2}");

        boolean areTheNewsFromToday = areTheNewsFromToday(urlString, pattern);

        return urlString.matches(".*/\\d{4}/\\d{2}/\\d{2}/\\d+_.*") && !FILTERS.matcher(urlString).matches()
                && areTheNewsFromToday && urlString.startsWith(SITE);
    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData htmlParseData && !page.getWebURL().toString().contains("paged=")) {
            String html = htmlParseData.getHtml();
            String url = String.valueOf(page.getWebURL());

            String heading = extractHeading(html);
            if (heading != null) {
                try {
                    saveNews(url, html);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void saveNews(String url, String html) throws URISyntaxException {
        String heading = extractHeading(html);
        String category = extractCategoryName(url);

        News news = new News();
        news.setNewsUrl(url);
        news.setHeading(heading);
        news.setCategory(category);
        news.setSiteName("dnevnik.bg");
        news.setCreatedDate(LocalDate.now().toString());

        newsRepository.save(news);
    }

    public String extractCategoryName(String url) throws URISyntaxException {
        if (url.startsWith(SITE)) {
            // Get the substring after "https://www.dnevnik.bg/"
            String remainingURL = url.substring(SITE.length());
            // Find the index of the next "/"
            int nextSlashIndex = remainingURL.indexOf("/");
            if (nextSlashIndex != -1) {
                // Extract the substring before the next "/"
                return remainingURL.substring(0, nextSlashIndex);
            } else {
                // If there is no "/", return the remaining string
                return remainingURL;
            }
        }
        return null;
    }

    public String extractHeading(String html) {
        Document document = Jsoup.parse(html);
        Element headingElement = document.selectFirst("h1[itemprop=\"name headline\"]");

        if (headingElement != null) {
            return headingElement.text();
        }

        headingElement = document.selectFirst("div.video-summary h3[itemprop=\"name description\"] > a");

        if (headingElement != null) {
            return headingElement.text();
        }
        return null;
    }

    private boolean areTheNewsFromToday(String urlString, Pattern pattern) {
        Matcher matcher = pattern.matcher(urlString);
        if (matcher.find()) {
            String dateString = matcher.group();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate date = LocalDate.parse(dateString, formatter);
            LocalDate targetDate = LocalDate.now();
            return date.isEqual(targetDate);
        }
        return false;
    }
}
