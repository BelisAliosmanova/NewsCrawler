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

/**
 * Клас за сканиране на уеб страници на Vesti.bg за новини.
 */
public class VestiBgCrawler extends WebCrawler {
    private final NewsFilter newsFilter; // Филтър за новини
    private final NewsRepository newsRepository; // Репозиторий за новини

    private final static String SITE = "https://www.vesti.bg/"; // Уеб сайт за сканиране
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" // Филтър за игнориране на определени файлови разширения
            + "|png|mp3|mp4|zip|gz))$");

    /**
     * Конструктор за инициализиране на обект от класа.
     *
     * @param newsFilter    Филтър за новини
     * @param newsRepository Репозиторий за новини
     */
    public VestiBgCrawler(NewsFilter newsFilter, NewsRepository newsRepository) {
        this.newsFilter = newsFilter;
        this.newsRepository = newsRepository;
    }

    /**
     * Проверява дали трябва да се посети дадена уеб страница.
     *
     * @param referringPage Отнасяща се страница
     * @param url           URL адрес на страницата
     * @return true, ако трябва да се посети страницата, в противен случай - false
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL();
        System.out.println("SHOULD VISIT " + urlString);

        return urlString.matches("https://www.vesti.bg/\\S+-\\d{7}") && !FILTERS.matcher(urlString).matches() && urlString.startsWith(SITE);
    }

    /**
     * Посещава дадена уеб страница и извлича новини от нея.
     *
     * @param page Посещавана страница
     */
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

    /**
     * Проверява дали новините са от днес.
     *
     * @param html HTML съдържание на страницата
     * @return true, ако новините са от днес, в противен случай - false
     */
    private boolean areTheNewsFromToday(String html) {
        String createdDate = extractArticleTime(html);
        LocalDate createdDateLocalDate = dateToLocalDate(createdDate);

        LocalDate today = LocalDate.now();

        return createdDateLocalDate.isEqual(today);
    }

    /**
     * Извлича датата на публикуване на статията.
     *
     * @param htmlContent HTML съдържание на страницата
     * @return Дата на публикуване на статията
     */
    public static String extractArticleTime(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);

        Element articleTimeElement = document.selectFirst("div.article-info > div.article-time");

        return articleTimeElement.text().trim();
    }

    /**
     * Преобразува дата от стринг в LocalDate обект.
     *
     * @param date Стрингова представка на датата
     * @return LocalDate обект, съдържащ датата
     */
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

    /**
     * Запазва новините в репозитория.
     *
     * @param url  URL адрес на статията
     * @param html HTML съдържание на статията
     * @throws URISyntaxException Грешка при невалиден URL адрес
     */
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

    /**
     * Извлича името на категорията от URL адреса на статията.
     *
     * @param url URL адрес на статията
     * @return Име на категорията
     * @throws URISyntaxException Грешка при невалиден URL адрес
     */
    public static String extractCategoryName(String url) throws URISyntaxException {
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

    /**
     * Извлича заглавието на статията от HTML съдържанието.
     *
     * @param html HTML съдържание на статията
     * @return Заглавие на статията
     */
    private String extractHeading(String html) {
        Document document = Jsoup.parse(html);
        Element headingElement = document.selectFirst("h1");

        if (headingElement != null) {
            return headingElement.text();
        }

        return null;
    }

    // Проверява дали новините са от днес
    public static boolean areTheNewsFromToday(String urlString, Pattern pattern) {
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
