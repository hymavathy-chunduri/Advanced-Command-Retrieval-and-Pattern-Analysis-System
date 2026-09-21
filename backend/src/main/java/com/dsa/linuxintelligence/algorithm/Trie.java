package com.dsa.linuxintelligence.algorithm;

import java.util.*;

/**
 * Academic DSA Implementation: Trie (Prefix Tree) Data Structure
 *
 * Purpose:
 * Provides lightning-fast O(K) prefix matching for autocomplete suggestions, where K is prefix length.
 *
 * Structure & Key Operations:
 * 1. Node Structure (TrieNode):
 *    - Map/Array of child nodes corresponding to characters ('a'-'z', '-', etc.).
 *    - Boolean flag (isEndOfWord) indicating complete command end.
 *    - Reference to stored command name / metadata object.
 *
 * 2. Insertion:
 *    - Iterates character-by-character from root down.
 *    - Dynamically instantiates missing child nodes.
 *    - Marks terminal node with command data.
 *
 * 3. Prefix Traversal:
 *    - Navigates down tree following prefix characters.
 *    - If prefix path breaks, prefix does not exist (returns empty list).
 *    - If path completes, reached prefix subtree root.
 *
 * 4. Collecting Suggestions:
 *    - Traverses subtree starting from prefix endpoint using Depth-First Search (DFS).
 *    - Collects matching valid commands up to specified suggestion limit.
 *
 * Complexity:
 * - Autocomplete Query Time: O(K + M) where K = prefix length, M = number of suggestions collected.
 * - Insertion Time: O(L) where L = command string length.
 * - Space Complexity: O(N * avg_length * alphabet_size) across dataset.
 */
public class Trie {

    public static class TrieNode {
        private final Map<Character, TrieNode> children;
        private boolean isEndOfWord;
        private String commandName;

        public TrieNode() {
            this.children = new HashMap<>();
            this.isEndOfWord = false;
            this.commandName = null;
        }

        public Map<Character, TrieNode> getChildren() { return children; }
        public boolean isEndOfWord() { return isEndOfWord; }
        public void setEndOfWord(boolean endOfWord) { isEndOfWord = endOfWord; }
        public String getCommandName() { return commandName; }
        public void setCommandName(String commandName) { this.commandName = commandName; }
    }

    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    /**
     * Inserts a command name into the Trie structure.
     *
     * @param command Command name (e.g. "mkdir")
     */
    public void insert(String command) {
        if (command == null || command.trim().isEmpty()) {
            return;
        }

        String normalized = command.trim().toLowerCase();
        TrieNode current = root;

        // Step 2: Insertion character by character
        for (char ch : normalized.toCharArray()) {
            current.getChildren().putIfAbsent(ch, new TrieNode());
            current = current.getChildren().get(ch);
        }

        current.setEndOfWord(true);
        current.setCommandName(command);
    }

    /**
     * Traverses the Trie to find the node corresponding to the given prefix.
     *
     * @param prefix Input prefix string (e.g. "mk")
     * @return TrieNode at prefix destination or null if prefix not found
     */
    public TrieNode navigateToPrefix(String prefix) {
        if (prefix == null) return null;

        String normalized = prefix.trim().toLowerCase();
        TrieNode current = root;

        // Step 3: Prefix Traversal
        for (char ch : normalized.toCharArray()) {
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return null; // Prefix path does not exist
            }
            current = node;
        }
        return current;
    }

    /**
     * Returns autocomplete suggestions starting with the given prefix.
     *
     * @param prefix Input search prefix
     * @param limit Maximum number of suggestions to return
     * @return List of matching command names
     */
    public List<String> getSuggestions(String prefix, int limit) {
        List<String> suggestions = new ArrayList<>();
        if (prefix == null || prefix.trim().isEmpty()) {
            return suggestions;
        }

        TrieNode prefixNode = navigateToPrefix(prefix);
        if (prefixNode == null) {
            return suggestions; // No matching prefix path
        }

        // Step 4: Collecting Suggestions via DFS traversal
        collectSuggestionsDFS(prefixNode, suggestions, limit);
        return suggestions;
    }

    /**
     * Depth-First Search helper to recursively gather terminal words.
     */
    private void collectSuggestionsDFS(TrieNode node, List<String> result, int limit) {
        if (node == null || result.size() >= limit) {
            return;
        }

        if (node.isEndOfWord()) {
            result.add(node.getCommandName());
        }

        // Traverse children sorted alphabetically for clean autocomplete order
        List<Character> sortedKeys = new ArrayList<>(node.getChildren().keySet());
        Collections.sort(sortedKeys);

        for (char ch : sortedKeys) {
            if (result.size() >= limit) break;
            collectSuggestionsDFS(node.getChildren().get(ch), result, limit);
        }
    }

    /**
     * Clears all entries from the Trie.
     */
    public void clear() {
        this.root.getChildren().clear();
    }
}
