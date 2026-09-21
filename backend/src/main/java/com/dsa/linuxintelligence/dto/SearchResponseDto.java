package com.dsa.linuxintelligence.dto;

import com.dsa.linuxintelligence.model.Command;
import java.util.List;

public class SearchResponseDto {
    private String query;
    private String algorithmUsed; // e.g. "Rabin-Karp Rolling Hash"
    private int matchCount;
    private long executionTimeMs;
    private List<Command> results;

    public SearchResponseDto() {}

    public SearchResponseDto(String query, String algorithmUsed, int matchCount, long executionTimeMs, List<Command> results) {
        this.query = query;
        this.algorithmUsed = algorithmUsed;
        this.matchCount = matchCount;
        this.executionTimeMs = executionTimeMs;
        this.results = results;
    }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }

    public int getMatchCount() { return matchCount; }
    public void setMatchCount(int matchCount) { this.matchCount = matchCount; }

    public long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public List<Command> getResults() { return results; }
    public void setResults(List<Command> results) { this.results = results; }
}
