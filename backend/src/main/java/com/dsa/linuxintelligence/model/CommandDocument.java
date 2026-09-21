package com.dsa.linuxintelligence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "linux_commands")
public class CommandDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String command;

    @Indexed
    private String category;

    @Field("short_definition")
    private String shortDefinition;

    private String description;

    private String syntax;

    private String example;

    @Field("example_explanation")
    private String exampleExplanation;

    @Field("common_options")
    private String commonOptions;

    @Field("related_commands")
    private String relatedCommands;

    @Field("safety_level")
    private String safetyLevel;

    private String distribution;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field("updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public CommandDocument() {}

    public CommandDocument(String id, String command, String category, String shortDefinition, String description,
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
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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
