package com.dsa.linuxintelligence.service;

import com.dsa.linuxintelligence.algorithm.*;
import com.dsa.linuxintelligence.dto.*;
import com.dsa.linuxintelligence.model.Command;
import com.dsa.linuxintelligence.repository.CommandRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommandSearchService {

    private static final Logger log = LoggerFactory.getLogger(CommandSearchService.class);

    private final CommandRepository commandRepository;

    // Manual DSA Algorithm Data Structures
    private final FastLookupCache fastLookupCache; // HashMap O(1) cache
    private final Trie trieAutocomplete;           // Trie prefix tree
    private boolean isInitialized = false;

    @Autowired
    public CommandSearchService(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
        this.fastLookupCache = new FastLookupCache();
        this.trieAutocomplete = new Trie();
    }

    /**
     * Initializes in-memory DSA data structures (HashMap & Trie) from PostgreSQL on backend startup.
     */
    @PostConstruct
    public synchronized void reloadIndexes() {
        log.info("Initializing DSA Data Structures (HashMap Cache & Trie Index) from PostgreSQL...");
        long start = System.currentTimeMillis();

        List<Command> allCommands = commandRepository.findAll();

        fastLookupCache.clear();
        trieAutocomplete.clear();

        for (Command cmd : allCommands) {
            fastLookupCache.put(cmd);
            trieAutocomplete.insert(cmd.getCommand());
        }

        isInitialized = true;
        long duration = System.currentTimeMillis() - start;
        log.info("DSA Initialization Complete! Cached {} commands into HashMap & Trie in {} ms.",
                fastLookupCache.size(), duration);
    }

    /**
     * 1. HashMap O(1) Fast Command Lookup
     */
    public Optional<Command> findCommandByName(String name) {
        if (!isInitialized) reloadIndexes();
        Command cmd = fastLookupCache.get(name);
        return Optional.ofNullable(cmd);
    }

    /**
     * 2. Rabin-Karp Rolling Hash Pattern Search
     * Searches across command names, definitions, descriptions, syntax, examples, options.
     */
    public SearchResponseDto searchByPattern(String query) {
        if (!isInitialized) reloadIndexes();
        long start = System.currentTimeMillis();

        List<Command> matchedCommands = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return new SearchResponseDto(query, "Rabin-Karp Rolling Hash", 0, 0, matchedCommands);
        }

        String targetPattern = query.trim();

        // Search through all cached command entities using manual Rabin-Karp
        for (Command cmd : fastLookupCache.getAllCommands()) {
            boolean isMatch = RabinKarpSearch.contains(cmd.getCommand(), targetPattern) ||
                    RabinKarpSearch.contains(cmd.getShortDefinition(), targetPattern) ||
                    RabinKarpSearch.contains(cmd.getDescription(), targetPattern) ||
                    RabinKarpSearch.contains(cmd.getSyntax(), targetPattern) ||
                    RabinKarpSearch.contains(cmd.getExample(), targetPattern) ||
                    RabinKarpSearch.contains(cmd.getCommonOptions(), targetPattern) ||
                    RabinKarpSearch.contains(cmd.getCategory(), targetPattern);

            if (isMatch) {
                matchedCommands.add(cmd);
            }
        }

        // Sort exact command name matches first, then alphabetically
        matchedCommands.sort((c1, c2) -> {
            boolean c1Exact = c1.getCommand().equalsIgnoreCase(targetPattern);
            boolean c2Exact = c2.getCommand().equalsIgnoreCase(targetPattern);
            if (c1Exact && !c2Exact) return -1;
            if (!c1Exact && c2Exact) return 1;
            return c1.getCommand().compareToIgnoreCase(c2.getCommand());
        });

        long executionTime = System.currentTimeMillis() - start;
        return new SearchResponseDto(query, "Rabin-Karp Rolling Hash", matchedCommands.size(), executionTime, matchedCommands);
    }

    /**
     * 3. Trie Prefix Autocomplete
     */
    public AutocompleteResponseDto getAutocompleteSuggestions(String prefix, int limit) {
        if (!isInitialized) reloadIndexes();

        List<String> suggestions = trieAutocomplete.getSuggestions(prefix, limit);
        return new AutocompleteResponseDto(
                prefix,
                "Trie Prefix Traversal (O(K))",
                suggestions.size(),
                suggestions
        );
    }

    /**
     * 4. Levenshtein Edit Distance Spelling Correction
     */
    public SpellingCorrectionDto correctSpelling(String query, int maxDistance, int limit) {
        if (!isInitialized) reloadIndexes();

        if (query == null || query.trim().isEmpty()) {
            return new SpellingCorrectionDto(query, "Levenshtein Dynamic Programming", false, Collections.emptyList());
        }

        String input = query.trim();

        // Check if query is already an exact match in HashMap
        if (fastLookupCache.contains(input)) {
            Command exactCmd = fastLookupCache.get(input);
            SpellingCorrectionDto.CorrectionItem exactItem = new SpellingCorrectionDto.CorrectionItem(
                    exactCmd.getCommand(), 0, exactCmd
            );
            return new SpellingCorrectionDto(input, "Levenshtein Dynamic Programming", true, Collections.singletonList(exactItem));
        }

        // Find candidates using manual Levenshtein DP
        Set<String> allCommandNames = fastLookupCache.getAllCommandNames();
        List<EditDistance.CorrectionCandidate> candidates = EditDistance.findBestMatches(
                input, allCommandNames, maxDistance, limit
        );

        List<SpellingCorrectionDto.CorrectionItem> suggestionItems = new ArrayList<>();
        for (EditDistance.CorrectionCandidate candidate : candidates) {
            Command cmd = fastLookupCache.get(candidate.getCommand());
            suggestionItems.add(new SpellingCorrectionDto.CorrectionItem(
                    candidate.getCommand(),
                    candidate.getDistance(),
                    cmd
            ));
        }

        return new SpellingCorrectionDto(
                input,
                "Levenshtein Dynamic Programming",
                !suggestionItems.isEmpty(),
                suggestionItems
        );
    }

    public FastLookupCache getFastLookupCache() {
        return fastLookupCache;
    }

    public Trie getTrieAutocomplete() {
        return trieAutocomplete;
    }
}
