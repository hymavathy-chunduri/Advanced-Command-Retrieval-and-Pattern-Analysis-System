package com.dsa.linuxintelligence.dto;

import com.dsa.linuxintelligence.model.Command;
import java.util.List;

public class SpellingCorrectionDto {

    public static class CorrectionItem {
        private String command;
        private int editDistance;
        private Command commandDetails;

        public CorrectionItem() {}

        public CorrectionItem(String command, int editDistance, Command commandDetails) {
            this.command = command;
            this.editDistance = editDistance;
            this.commandDetails = commandDetails;
        }

        public String getCommand() { return command; }
        public void setCommand(String command) { this.command = command; }

        public int getEditDistance() { return editDistance; }
        public void setEditDistance(int editDistance) { this.editDistance = editDistance; }

        public Command getCommandDetails() { return commandDetails; }
        public void setCommandDetails(Command commandDetails) { this.commandDetails = commandDetails; }
    }

    private String originalInput;
    private String algorithmUsed; // e.g. "Levenshtein Dynamic Programming"
    private boolean corrected;
    private List<CorrectionItem> suggestions;

    public SpellingCorrectionDto() {}

    public SpellingCorrectionDto(String originalInput, String algorithmUsed, boolean corrected, List<CorrectionItem> suggestions) {
        this.originalInput = originalInput;
        this.algorithmUsed = algorithmUsed;
        this.corrected = corrected;
        this.suggestions = suggestions;
    }

    public String getOriginalInput() { return originalInput; }
    public void setOriginalInput(String originalInput) { this.originalInput = originalInput; }

    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }

    public boolean isCorrected() { return corrected; }
    public void setCorrected(boolean corrected) { this.corrected = corrected; }

    public List<CorrectionItem> getSuggestions() { return suggestions; }
    public void setSuggestions(List<CorrectionItem> suggestions) { this.suggestions = suggestions; }
}
