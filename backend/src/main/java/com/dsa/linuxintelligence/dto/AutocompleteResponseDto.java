package com.dsa.linuxintelligence.dto;

import java.util.List;

public class AutocompleteResponseDto {
    private String prefix;
    private String algorithmUsed; // e.g. "Trie Prefix Traversal (O(K))"
    private int suggestionCount;
    private List<String> suggestions;

    public AutocompleteResponseDto() {}

    public AutocompleteResponseDto(String prefix, String algorithmUsed, int suggestionCount, List<String> suggestions) {
        this.prefix = prefix;
        this.algorithmUsed = algorithmUsed;
        this.suggestionCount = suggestionCount;
        this.suggestions = suggestions;
    }

    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }

    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }

    public int getSuggestionCount() { return suggestionCount; }
    public void setSuggestionCount(int suggestionCount) { this.suggestionCount = suggestionCount; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
}
