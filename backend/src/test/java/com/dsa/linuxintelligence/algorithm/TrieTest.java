package com.dsa.linuxintelligence.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrieTest {

    private Trie trie;

    @BeforeEach
    public void setUp() {
        trie = new Trie();
        trie.insert("mkdir");
        trie.insert("mktemp");
        trie.insert("mkfs");
        trie.insert("grep");
        trie.insert("groupadd");
    }

    @Test
    public void testAutocompleteMk() {
        List<String> suggestions = trie.getSuggestions("mk", 10);
        assertEquals(3, suggestions.size());
        assertTrue(suggestions.contains("mkdir"));
        assertTrue(suggestions.contains("mktemp"));
        assertTrue(suggestions.contains("mkfs"));
    }

    @Test
    public void testAutocompleteGr() {
        List<String> suggestions = trie.getSuggestions("gr", 10);
        assertEquals(2, suggestions.size());
        assertTrue(suggestions.contains("grep"));
        assertTrue(suggestions.contains("groupadd"));
    }

    @Test
    public void testAutocompleteNonExistentPrefix() {
        List<String> suggestions = trie.getSuggestions("xyz", 10);
        assertTrue(suggestions.isEmpty());
    }

    @Test
    public void testEmptyPrefix() {
        List<String> suggestions = trie.getSuggestions("", 10);
        assertTrue(suggestions.isEmpty());
    }
}
