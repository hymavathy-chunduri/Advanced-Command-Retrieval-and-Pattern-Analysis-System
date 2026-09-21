package com.dsa.linuxintelligence.algorithm;

import java.util.*;

/**
 * Academic DSA Implementation: Levenshtein Edit Distance Algorithm
 *
 * Purpose:
 * Computes minimum edit operations (insertions, deletions, substitutions) required
 * to transform one string into another. Used for typo detection and spelling correction.
 *
 * Algorithm Strategy: Dynamic Programming (DP)
 *
 * DP State Definition:
 * dp[i][j] represents the minimum edit distance between prefix str1[0...i-1] and str2[0...j-1].
 *
 * Recurrence Relation:
 * If str1[i-1] == str2[j-1]:
 *    dp[i][j] = dp[i-1][j-1] (no cost operation)
 * Else:
 *    dp[i][j] = 1 + MIN(
 *        dp[i][j-1],   // Insertion operation cost
 *        dp[i-1][j],   // Deletion operation cost
 *        dp[i-1][j-1]  // Replacement / Substitution operation cost
 *    )
 *
 * Operations Breakdown:
 * 1. Insertion: Adds character to match target.
 * 2. Deletion: Removes surplus character from input.
 * 3. Replacement: Replaces mismatched character with target character.
 * 4. Minimum Distance: Takes minimum of insertion, deletion, and replacement paths.
 *
 * Complexity:
 * - Time Complexity: O(M * N) for two strings of length M and N.
 * - Space Complexity: O(M * N) matrix memory (can be optimized to O(N)).
 */
public class EditDistance {

    /**
     * Data Transfer Class representing a correction candidate match.
     */
    public static class CorrectionCandidate implements Comparable<CorrectionCandidate> {
        private final String command;
        private final int distance;

        public CorrectionCandidate(String command, int distance) {
            this.command = command;
            this.distance = distance;
        }

        public String getCommand() { return command; }
        public int getDistance() { return distance; }

        @Override
        public int compareTo(CorrectionCandidate other) {
            // Sort by distance ascending, then command name alphabetically
            if (this.distance != other.distance) {
                return Integer.compare(this.distance, other.distance);
            }
            return this.command.compareTo(other.command);
        }
    }

    /**
     * Calculates exact Levenshtein Edit Distance between two strings.
     *
     * @param str1 Input search string (e.g. "grpe")
     * @param str2 Target command string (e.g. "grep")
     * @return Integer edit distance count
     */
    public static int compute(String str1, String str2) {
        if (str1 == null && str2 == null) return 0;
        if (str1 == null) return str2.length();
        if (str2 == null) return str1.length();

        String s1 = str1.trim().toLowerCase();
        String s2 = str2.trim().toLowerCase();

        int m = s1.length();
        int n = s2.length();

        // Step 1: DP Table initialization
        int[][] dp = new int[m + 1][n + 1];

        // Base cases: transforming empty string to non-empty string
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Deleting all i characters
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Inserting all j characters
        }

        // Step 2 & 3: Fill DP table row by row
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    // Match: no operation needed
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insertionCost = dp[i][j - 1] + 1;      // Insertion
                    int deletionCost = dp[i - 1][j] + 1;       // Deletion
                    int replacementCost = dp[i - 1][j - 1] + 1; // Replacement

                    // Step 4: Minimum distance selection
                    dp[i][j] = Math.min(insertionCost, Math.min(deletionCost, replacementCost));
                }
            }
        }

        return dp[m][n];
    }

    /**
     * Searches a collection of valid command names and finds closest spelling matches.
     *
     * @param input Misspelled input string (e.g. "grpe")
     * @param commandNames List of valid target command names
     * @param maxDistance Maximum threshold distance allowed for a match
     * @param limit Maximum number of suggestions to return
     * @return List of CorrectionCandidate objects ordered by closeness
     */
    public static List<CorrectionCandidate> findBestMatches(String input, Collection<String> commandNames, int maxDistance, int limit) {
        List<CorrectionCandidate> candidates = new ArrayList<>();

        if (input == null || input.trim().isEmpty() || commandNames == null) {
            return candidates;
        }

        String targetInput = input.trim().toLowerCase();

        for (String command : commandNames) {
            int dist = compute(targetInput, command);
            if (dist <= maxDistance) {
                candidates.add(new CorrectionCandidate(command, dist));
            }
        }

        Collections.sort(candidates);

        if (candidates.size() > limit) {
            return new ArrayList<>(candidates.subList(0, limit));
        }

        return candidates;
    }
}
