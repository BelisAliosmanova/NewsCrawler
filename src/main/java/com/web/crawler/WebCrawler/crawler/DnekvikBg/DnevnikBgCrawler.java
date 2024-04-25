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
    private final NewsFilter newsFilter; // Филтър за новини

    private final NewsRepository newsRepository; // Репозиторий за новини

    private final static String SITE = "https://www.dnevnik.bg/"; // Основен уебсайт

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$"); // Филтър за файлови разширения, които да се игнорират

    public DnevnikBgCrawler(NewsFilter newsFilter, NewsRepository newsRepository) {
        this.newsFilter = newsFilter;
        this.newsRepository = newsRepository;
    }

    // Проверява дали трябва да се посети даден URL адрес
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL();
        Pattern pattern = Pattern.compile("\\d{4}/\\d{2}/\\d{2}");

        boolean areTheNewsFromToday = areTheNewsFromToday(urlString, pattern); // Проверка дали новините са от днес

        return urlString.matches(".*/\\d{4}/\\d{2}/\\d{2}/\\d+_.*") && !FILTERS.matcher(urlString).matches()
                && areTheNewsFromToday && urlString.startsWith(SITE);
    }

    // Посещава страницата и обработва нейното съдържание
    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData htmlParseData && !page.getWebURL().toString().contains("paged=")) {
            String html = htmlParseData.getHtml();
            String url = String.valueOf(page.getWebURL());

            String heading = extractHeading(html);
            if (heading != null) {
                try {
                    saveNews(url, html); // Запазва новината в базата данни
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // Запазва новината в базата данни
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

    // Извлича категорията на новината от URL адреса
    public String extractCategoryName(String url) throws URISyntaxException {
        if (url.startsWith(SITE)) {
            // Взима частта от URL адреса след "https://www.dnevnik.bg/"
            String remainingURL = url.substring(SITE.length());
            // Намира индекса на следващия "/"
            int nextSlashIndex = remainingURL.indexOf("/");
            if (nextSlashIndex != -1) {
                // Извлича частта от URL адреса преди следващия "/"
                return remainingURL.substring(0, nextSlashIndex);
            } else {
                // Ако няма "/", връща останалата част от URL адреса
                return remainingURL;
            }
        }
        return null;
    }

    // Извлича заглавието на новината от HTML кода на страницата
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

    // Проверява дали новината е от днес
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