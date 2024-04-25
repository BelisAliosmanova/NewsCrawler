package com.web.crawler.WebCrawler.service.impl;

import com.web.crawler.WebCrawler.entities.Keyword;
import com.web.crawler.WebCrawler.entities.KeywordOccurrence;
import com.web.crawler.WebCrawler.entities.News;
import com.web.crawler.WebCrawler.entities.User;
import com.web.crawler.WebCrawler.repositories.KeywordOccurrenceRepository;
import com.web.crawler.WebCrawler.repositories.KeywordRepository;
import com.web.crawler.WebCrawler.repositories.NewsRepository;
import com.web.crawler.WebCrawler.repositories.UserRepository;
import com.web.crawler.WebCrawler.service.KeywordOccurrenceService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class KeywordOccurrenceServiceImpl implements KeywordOccurrenceService {

    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;

    private final NewsRepository newsRepository;
    private final KeywordOccurrenceRepository keywordOccurrenceRepository;

    public KeywordOccurrenceServiceImpl(KeywordRepository keywordRepository, UserRepository userRepository, NewsRepository newsRepository, KeywordOccurrenceRepository keywordOccurrenceRepository) {
        this.keywordRepository = keywordRepository;
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.keywordOccurrenceRepository = keywordOccurrenceRepository;
    }

    @Override
    public List<KeywordOccurrence> allKeywordOccurrencesBuLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        List<KeywordOccurrence> allKeywordOccurrences = keywordOccurrenceRepository.findAll();
        List<KeywordOccurrence> loggedUserKeywordOccurrences = keywordOccurrenceRepository.findAll();

        for (KeywordOccurrence keywordOccurrence : allKeywordOccurrences) {
            if (!Objects.equals(keywordOccurrence.getKeywordId().getUserId().getId(), user.getId())) {
                loggedUserKeywordOccurrences.remove(keywordOccurrence);
            }
        }
        return loggedUserKeywordOccurrences;
    }

    // Прави статистика на ключовите думи
    @Override
    public void generateDailyKeywordStatistics() {
        // Пряви статистика само на днескашните думи
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDateStr = currentDate.format(formatter);

        List<Keyword> keywords = keywordRepository.findAllByDeletedFalse();
        List<News> newsList = newsRepository.findByCreatedDate(currentDateStr);

        for (Keyword keyword : keywords) {
            int occurrence = 0;

            for (News news : newsList) {
                // Гледаме да няма значение дали буквите са малки или големи
                keyword.setName(keyword.getName().toLowerCase());
                news.setHeading(news.getHeading().toLowerCase());

                // Проверяваме дали новината съдържа ключовата дума
                if (news.getHeading().contains(keyword.getName())) {
                    occurrence++;
                }
            }
            if (occurrence != 0) {
                KeywordOccurrence keywordOccurrence = new KeywordOccurrence();
                keywordOccurrence.setKeywordId(keyword);
                keywordOccurrence.setDailyOccurrences(occurrence);
                keywordOccurrence.setDate(LocalDate.now().toString());

                // Запаметяваме срещнатата дума заедно с това, колко пъти се среща
                List<KeywordOccurrence> allKeywordOccurrences = keywordOccurrenceRepository.findAll();
                if (!allKeywordOccurrences.isEmpty()) {
                    boolean isAlreadySaved = false;
                    for (KeywordOccurrence keywordOccurrenceCheck : allKeywordOccurrences) {
                        if (Objects.equals(keywordOccurrenceCheck.getKeywordId().getId(), keyword.getId())) {
                            isAlreadySaved = true;
                            break;
                        }
                    }
                    if (!isAlreadySaved) {
                        keywordOccurrenceRepository.save(keywordOccurrence);
                    }
                } else {
                    keywordOccurrenceRepository.save(keywordOccurrence);
                }
            }
        }
    }
}
