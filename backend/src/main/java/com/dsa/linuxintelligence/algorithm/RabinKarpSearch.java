package com.dsa.linuxintelligence.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Academic DSA Implementation: Rabin-Karp String Pattern Searching Algorithm
 *
 * Algorithm Concept:
 * Uses a polynomial rolling hash function to compute a numerical hash signature for the pattern
 * and every sliding window of equal length in the text.
 *
 * Key Steps:
 * 1. Pattern Hash: Compute hash for pattern string of length M using base B and prime modulo Q.
 * 2. Rolling Hash: Slide window over text of length N. Efficiently update hash in O(1) time
 *    by removing outgoing high-order character and adding incoming low-order character.
 * 3. Hash Comparison: Compare window hash with pattern hash in O(1).
 * 4. Character Verification: If hash signatures match, perform explicit character-by-character
 *    verification to eliminate false positives caused by hash collisions.
 *
 * Complexity:
 * - Average Time Complexity: O(N + M) where N = text length, M = pattern length.
 * - Worst Case Complexity: O(N * M) (very rare with good prime modulo).
 * - Space Complexity: O(1) auxiliary memory.
 */
public class RabinKarpSearch {

    // Prime number modulus to avoid overflow and reduce collisions
    private static final int PRIME = 101;
    // Alphabet size / Radix base (ASCII character set size)
    private static final int BASE = 256;

    /**
     * Searches for occurrences of a pattern within a text string.
     *
     * @param text The full text document or metadata string to search inside
     * @param pattern The search query pattern
     * @return List of 0-based starting indices where pattern occurs in text
     */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();

        if (text == null || pattern == null) {
            return matches;
        }

        String textLower = text.toLowerCase();
        String patternLower = pattern.toLowerCase();

        int n = textLower.length();
        int m = patternLower.length();

        // Base cases: empty pattern or pattern longer than target text
        if (m == 0 || m > n) {
            return matches;
        }

        long patternHash = 0; // Hash value for pattern
        long textHash = 0;    // Hash value for text sliding window
        long h = 1;           // The multiplier factor for high-order digit: (BASE^(m-1)) % PRIME

        // Step 1: Pre-compute h = pow(BASE, m-1) % PRIME
        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % PRIME;
        }

        // Step 2: Compute initial pattern hash and initial text window hash
        for (int i = 0; i < m; i++) {
            patternHash = (BASE * patternHash + patternLower.charAt(i)) % PRIME;
            textHash = (BASE * textHash + textLower.charAt(i)) % PRIME;
        }

        // Step 3: Slide pattern over text one by one
        for (int i = 0; i <= n - m; i++) {

            // Hash comparison check
            if (patternHash == textHash) {
                // Character verification step to handle hash collisions
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (textLower.charAt(i + j) != patternLower.charAt(j)) {
                        match = false; // Collision detected!
                        break;
                    }
                }
                if (match) {
                    matches.add(i); // Valid pattern match verified
                }
            }

            // Rolling Hash Step: Calculate hash value for next sliding window
            if (i < n - m) {
                // Remove leading digit, add trailing digit
                textHash = (BASE * (textHash - textLower.charAt(i) * h) + textLower.charAt(i + m)) % PRIME;

                // We might get negative value of textHash, converting it to positive
                if (textHash < 0) {
                    textHash = (textHash + PRIME);
                }
            }
        }

        return matches;
    }

    /**
     * Helper method to check if text contains pattern using Rabin-Karp algorithm.
     */
    public static boolean contains(String text, String pattern) {
        return !search(text, pattern).isEmpty();
    }
}
