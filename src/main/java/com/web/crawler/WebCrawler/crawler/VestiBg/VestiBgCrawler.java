package com.web.crawler.WebCrawler.crawler.VestiBg;

import com.web.crawler.WebCrawler.constants.Month;
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

public class VestiBgCrawler extends WebCrawler {
    private final NewsFilter newsFilter;

    private final NewsRepository newsRepository;

    private final static String SITE = "https://www.vesti.bg/";

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    public VestiBgCrawler(NewsFilter newsFilter, NewsRepository newsRepository) {
        this.newsFilter = newsFilter;
        this.newsRepository = newsRepository;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL();
        System.out.println("SHOULD VISIT " + urlString);

        return urlString.matches("https://www.vesti.bg/\\S+-\\d{7}") && !FILTERS.matcher(urlString).matches() && urlString.startsWith(SITE);
    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData htmlParseData) {
            String html = htmlParseData.getHtml();
            String url = String.valueOf(page.getWebURL());

            boolean areTheNewsFromToday = areTheNewsFromToday(html);
            if (areTheNewsFromToday) {
                System.out.println("VISIT " + url);
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
    }

    private boolean areTheNewsFromToday(String html) {
        String createdDate = extractArticleTime(html);
        LocalDate createdDateLocalDate = dateToLocalDate(createdDate);

        LocalDate today = LocalDate.now();

        return createdDateLocalDate.isEqual(today);
    }

    private static String extractArticleTime(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);

        Element articleTimeElement = document.selectFirst("div.article-info > div.article-time");

        return articleTimeElement.text().trim();
    }

    public static LocalDate dateToLocalDate(String date) {
        try {
            String[] dateElements = date.split(",")[0].split("\\s+");
            int day = Integer.parseInt(dateElements[0]);
            int year = Integer.parseInt(dateElements[2]);
            Month month = Month.getMonth(dateElements[1]);

            if (month == null) {
                return null;
            }

            return LocalDate.of(year, month.getMonthRow(), day);
        } catch (Exception e) {
            return null;
        }
    }

    private void saveNews(String url, String html) throws URISyntaxException {
        String heading = extractHeading(html);
        String category = extractCategoryName(url);

        News news = new News();
        news.setNewsUrl(url);
        news.setHeading(heading);
        news.setCategory(category);
        news.setSiteName("vesti.bg");
        news.setCreatedDate(LocalDate.now().toString());

        newsRepository.save(news);
    }

    private String extractCategoryName(String url) throws URISyntaxException {
        if (url.startsWith(SITE)) {
            // Get the substring after "https://www.vesti.bg/"
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

    private String extractHeading(String html) {
        Document document = Jsoup.parse(html);
        Element headingElement = document.selectFirst("h1");

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
