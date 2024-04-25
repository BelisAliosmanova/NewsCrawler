package com.web.crawler.WebCrawler.service.impl;

import com.web.crawler.WebCrawler.entities.Keyword;
import com.web.crawler.WebCrawler.entities.User;
import com.web.crawler.WebCrawler.repositories.KeywordRepository;
import com.web.crawler.WebCrawler.repositories.UserRepository;
import com.web.crawler.WebCrawler.service.KeywordOccurrenceService;
import com.web.crawler.WebCrawler.service.KeywordService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

// CRUD операции за ключовите думи
@Service
public class KeywordServiceImpl implements KeywordService {
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final KeywordOccurrenceService keywordOccurrenceService;

    public KeywordServiceImpl(KeywordRepository keywordRepository, UserRepository userRepository, KeywordOccurrenceService keywordOccurrenceService) {
        this.keywordRepository = keywordRepository;
        this.userRepository = userRepository;
        this.keywordOccurrenceService = keywordOccurrenceService;
    }

    // Извлича всички ключови думи на текущия/логнатия потребител
    @Override
    public List<Keyword> getAllKeywordsForUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        List<Keyword> keywords = keywordRepository.findAllByDeletedFalse();

        //Цикълът while е заменен с 'Collection.removeIf'(така препоръча интелиждей)
        keywords.removeIf(keyword -> !Objects.equals(keyword.getUserId().getId(), user.getId()));

        return keywords;
    }

    @Override
    public Keyword getKeywordById(Long id) {
        Optional<Keyword> keyword = keywordRepository.findByIdAndDeletedFalse(id);
        if (keyword.isPresent()) {
            return keyword.get();
        }
        throw new EntityNotFoundException("The keyword is not found!");
    }

    @Override
    public Keyword createKeyword(Keyword keyword) {
        // Вземи логнатия потребител
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        keyword.setUserId(user);
        Keyword savedKeyword = keywordRepository.save(keyword);
        keywordOccurrenceService.generateDailyKeywordStatistics();
        return savedKeyword;
    }

    @Override
    public Keyword updateKeyword(Long id, Keyword newKeyword) {
        Optional<Keyword> existingKeywordOptional = keywordRepository.findByIdAndDeletedFalse(id);

        if (existingKeywordOptional.isEmpty()) {
            throw new EntityNotFoundException("The keyword is not found!");
        }
        newKeyword.setUserId(existingKeywordOptional.get().getUserId());

        return keywordRepository.save(newKeyword);
    }

    @Override
    public void deleteKeyword(Long id) {
        Optional<Keyword> keyword = keywordRepository.findByIdAndDeletedFalse(id);
        if (keyword.isPresent()) {
            keyword.get().setDeleted(true);
            keywordRepository.save(keyword.get());
        } else {
            throw new EntityNotFoundException("The keyword is not found!");
        }
    }
}