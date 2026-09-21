package com.dsa.linuxintelligence.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "linux_commands", indexes = {
    @Index(name = "idx_linux_commands_command", columnList = "command"),
    @Index(name = "idx_linux_commands_category", columnList = "category")
})
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String command;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(name = "short_definition", nullable = false, columnDefinition = "TEXT")
    private String shortDefinition;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String syntax;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String example;

    @Column(name = "example_explanation", nullable = false, columnDefinition = "TEXT")
    private String exampleExplanation;

    @Column(name = "common_options", nullable = false, columnDefinition = "TEXT")
    private String commonOptions;

    @Column(name = "related_commands", nullable = false, columnDefinition = "TEXT")
    private String relatedCommands;

    @Column(name = "safety_level", length = 30)
    private String safetyLevel;

    @Column(length = 100)
    private String distribution;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Command() {}

    public Command(Long id, String command, String category, String shortDefinition, String description,
                   String syntax, String example, String exampleExplanation, String commonOptions,
                   String relatedCommands, String safetyLevel, String distribution) {
        this.id = id;
        this.command = command;
        this.category = category;
        this.shortDefinition = shortDefinition;
        this.description = description;
        this.syntax = syntax;
        this.example = example;
        this.exampleExplanation = exampleExplanation;
        this.commonOptions = commonOptions;
        this.relatedCommands = relatedCommands;
        this.safetyLevel = safetyLevel;
        this.distribution = distribution;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCommand() { return command; }
    public void setCommand(String command) { this.command = command; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getShortDefinition() { return shortDefinition; }
    public void setShortDefinition(String shortDefinition) { this.shortDefinition = shortDefinition; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSyntax() { return syntax; }
    public void setSyntax(String syntax) { this.syntax = syntax; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public String getExampleExplanation() { return exampleExplanation; }
    public void setExampleExplanation(String exampleExplanation) { this.exampleExplanation = exampleExplanation; }

    public String getCommonOptions() { return commonOptions; }
    public void setCommonOptions(String commonOptions) { this.commonOptions = commonOptions; }

    public String getRelatedCommands() { return relatedCommands; }
    public void setRelatedCommands(String relatedCommands) { this.relatedCommands = relatedCommands; }

    public String getSafetyLevel() { return safetyLevel; }
    public void setSafetyLevel(String safetyLevel) { this.safetyLevel = safetyLevel; }

    public String getDistribution() { return distribution; }
    public void setDistribution(String distribution) { this.distribution = distribution; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
