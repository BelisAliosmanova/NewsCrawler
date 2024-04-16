package com.web.crawler.WebCrawler.service.impl;

import com.web.crawler.WebCrawler.entities.Keyword;
import com.web.crawler.WebCrawler.entities.User;
import com.web.crawler.WebCrawler.repositories.KeywordRepository;
import com.web.crawler.WebCrawler.repositories.UserRepository;
import com.web.crawler.WebCrawler.service.KeywordService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KeywordServiceImpl implements KeywordService {
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;

    public KeywordServiceImpl(KeywordRepository keywordRepository, UserRepository userRepository) {
        this.keywordRepository = keywordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Keyword> getAllKeywordsForUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        // Return only logged user's keywords
        List<Keyword> keywords = keywordRepository.findAll();

        //Replaced while loop 'Collection.removeIf' suggested from the IJ
        keywords.removeIf(keyword -> keyword.getUserId().getId() != user.getId());

        return keywords;
    }

    @Override
    public Keyword getKeywordById(Long id) {
        Optional<Keyword> keyword = keywordRepository.findById(id);
        if (keyword.isPresent()) {
            return keyword.get();
        }
        throw new EntityNotFoundException("The keyword is not found!");
    }

    @Override
    public Keyword createKeyword(Keyword keyword) {
        // Get the logged user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        keyword.setUserId(user);
        return keywordRepository.save(keyword);
    }

    @Override
    public Keyword updateKeyword(Long id, Keyword newKeyword) {
        Optional<Keyword> existingKeywordOptional = keywordRepository.findById(id);

        if (existingKeywordOptional.isEmpty()) {
            throw new EntityNotFoundException("The keyword is not found!");
        }
        newKeyword.setUserId(existingKeywordOptional.get().getUserId());

        return keywordRepository.save(newKeyword);
    }

    @Override
    public void deleteKeyword(Long id) {
        Optional<Keyword> keyword = keywordRepository.findById(id);
        if (keyword.isPresent()) {
            keywordRepository.delete(keyword.get());
        } else {
            throw new EntityNotFoundException("The keyword is not found!");
        }
    }
}