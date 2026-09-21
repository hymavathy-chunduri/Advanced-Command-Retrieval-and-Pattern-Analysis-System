package com.dsa.linuxintelligence.dto;

import java.util.List;

public class SystemStatsDto {
    private long totalCommands;
    private long totalCategories;
    private int memoryCacheSize;
    private String cacheStatus;
    private List<CategoryStatsDto> categoryDistribution;

    public SystemStatsDto() {}

    public SystemStatsDto(long totalCommands, long totalCategories, int memoryCacheSize, String cacheStatus, List<CategoryStatsDto> categoryDistribution) {
        this.totalCommands = totalCommands;
        this.totalCategories = totalCategories;
        this.memoryCacheSize = memoryCacheSize;
        this.cacheStatus = cacheStatus;
        this.categoryDistribution = categoryDistribution;
    }

    public long getTotalCommands() { return totalCommands; }
    public void setTotalCommands(long totalCommands) { this.totalCommands = totalCommands; }

    public long getTotalCategories() { return totalCategories; }
    public void setTotalCategories(long totalCategories) { this.totalCategories = totalCategories; }

    public int getMemoryCacheSize() { return memoryCacheSize; }
    public void setMemoryCacheSize(int memoryCacheSize) { this.memoryCacheSize = memoryCacheSize; }

    public String getCacheStatus() { return cacheStatus; }
    public void setCacheStatus(String cacheStatus) { this.cacheStatus = cacheStatus; }

    public List<CategoryStatsDto> getCategoryDistribution() { return categoryDistribution; }
    public void setCategoryDistribution(List<CategoryStatsDto> categoryDistribution) { this.categoryDistribution = categoryDistribution; }
}
