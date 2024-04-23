package com.web.crawler.WebCrawler.controller;

import com.web.crawler.WebCrawler.controllers.KeywordController;
import com.web.crawler.WebCrawler.entities.Keyword;
import com.web.crawler.WebCrawler.entities.KeywordOccurrence;
import com.web.crawler.WebCrawler.service.KeywordOccurrenceService;
import com.web.crawler.WebCrawler.service.KeywordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeywordControllerTest {

    @Mock
    private KeywordService keywordService;

    @Mock
    private KeywordOccurrenceService keywordOccurrenceService;

    @Mock
    private Model model;

    @InjectMocks
    private KeywordController keywordController;

    @Test
    public void testGetAllKeywords() {
        List<Keyword> keywords = List.of();
        when(keywordService.getAllKeywordsForUser()).thenReturn(keywords);

        String viewName = keywordController.getAllKeywords(model);

        assertThat(viewName).isEqualTo("keyword/list");
        verify(model).addAttribute("keywords", keywords);
    }

    @Test
    public void testGetAllKeywordOccurrences() {
        List<KeywordOccurrence> keywordOccurrences = Arrays.asList(new KeywordOccurrence(), new KeywordOccurrence());
        when(keywordOccurrenceService.allKeywordOccurrencesBuLoggedUser()).thenReturn(keywordOccurrences);

        String viewName = keywordController.getAllKeywordOccurrences(model);

        assertThat(viewName).isEqualTo("keywordOccurrences/list");
        verify(model).addAttribute("keywordOccurrences", keywordOccurrences);
    }

    @Test
    public void testGetKeywordById() {
        long keywordId = 1L;
        Keyword keyword = new Keyword();
        when(keywordService.getKeywordById(keywordId)).thenReturn(keyword);

        String viewName = keywordController.getKeywordById(keywordId, model);

        assertThat(viewName).isEqualTo("keyword/details");
        verify(model).addAttribute("keyword", keyword);
    }

    @Test
    public void testShowCreateForm() {
        List<Keyword> keywords = Arrays.asList(new Keyword(), new Keyword());
        when(keywordService.getAllKeywordsForUser()).thenReturn(keywords);

        String viewName = keywordController.showCreateForm(model);

        assertThat(viewName).isEqualTo("keyword/form");
        verify(model).addAttribute("keywords", keywords);
    }

    @Test
    public void testCreateKeyword() {
        Keyword keyword = new Keyword();

        String viewName = keywordController.createKeyword(keyword);

        assertThat(viewName).isEqualTo("redirect:/keywords");
        verify(keywordService).createKeyword(keyword);
    }

    @Test
    public void testShowEditForm() {
        long keywordId = 1L;
        Keyword keyword = new Keyword();
        when(keywordService.getKeywordById(keywordId)).thenReturn(keyword);
        List<Keyword> keywords = Arrays.asList(new Keyword(), new Keyword());
        when(keywordService.getAllKeywordsForUser()).thenReturn(keywords);

        String viewName = keywordController.showEditForm(keywordId, model);

        assertThat(viewName).isEqualTo("keyword/edit");
        verify(model).addAttribute("keyword", keyword);
        verify(model).addAttribute("keywords", keywords);
    }

    @Test
    public void testUpdateKeyword() {
        long keywordId = 1L;
        Keyword keyword = new Keyword();

        String viewName = keywordController.updateKeyword(keywordId, keyword);

        assertThat(viewName).isEqualTo("redirect:/keywords");
        verify(keywordService).updateKeyword(keywordId, keyword);
    }

    @Test
    public void testDeleteKeyword() {
        long keywordId = 1L;

        String viewName = keywordController.deleteKeyword(keywordId);

        assertThat(viewName).isEqualTo("redirect:/keywords");
        verify(keywordService).deleteKeyword(keywordId);
    }
}