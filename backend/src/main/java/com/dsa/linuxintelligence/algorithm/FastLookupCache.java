package com.dsa.linuxintelligence.algorithm;

import com.dsa.linuxintelligence.model.Command;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Academic DSA Implementation: In-Memory Fast Lookup Cache Layer using HashMap
 *
 * System Architecture Role:
 * - PostgreSQL = Permanent persistent storage of truth.
 * - HashMap (FastLookupCache) = Fast runtime O(1) in-memory command lookup layer.
 * - Trie = Fast O(K) prefix search index for autocomplete.
 * - Rabin-Karp = Rolling-hash string pattern matching across metadata fields.
 * - Edit Distance = Dynamic programming spelling correction index.
 *
 * HashMap Mechanics:
 * - Key: Command name String (e.g., "grep", "mkdir") normalized to lowercase.
 * - Value: Complete Command entity object containing full metadata.
 * - Insertion: O(1) average time complexity using hash bucket distribution.
 * - Retrieval: O(1) average time complexity bypassing database I/O overhead.
 */
public class FastLookupCache {

    // Thread-safe ConcurrentHashMap for concurrent web request access
    private final Map<String, Command> cacheMap;

    public FastLookupCache() {
        this.cacheMap = new ConcurrentHashMap<>();
    }

    /**
     * Inserts or updates a command in the in-memory HashMap cache.
     *
     * @param command Command object to insert
     */
    public void put(Command command) {
        if (command == null || command.getCommand() == null) return;
        cacheMap.put(command.getCommand().toLowerCase(), command);
    }

    /**
     * Bulk inserts a collection of commands into the cache.
     *
     * @param commands Collection of Command objects
     */
    public void putAll(Collection<Command> commands) {
        if (commands == null) return;
        for (Command cmd : commands) {
            put(cmd);
        }
    }

    /**
     * Performs O(1) fast lookup for a command by key name.
     *
     * @param commandName Name of command (case-insensitive)
     * @return Command object or null if not found in cache
     */
    public Command get(String commandName) {
        if (commandName == null) return null;
        return cacheMap.get(commandName.trim().toLowerCase());
    }

    /**
     * Checks if a command exists in the fast lookup cache.
     */
    public boolean contains(String commandName) {
        if (commandName == null) return false;
        return cacheMap.containsKey(commandName.trim().toLowerCase());
    }

    /**
     * Returns all command names currently stored in the cache key set.
     */
    public Set<String> getAllCommandNames() {
        return Collections.unmodifiableSet(cacheMap.keySet());
    }

    /**
     * Returns all command objects stored in the cache value collection.
     */
    public Collection<Command> getAllCommands() {
        return Collections.unmodifiableCollection(cacheMap.values());
    }

    /**
     * Returns the total count of commands currently cached in RAM.
     */
    public int size() {
        return cacheMap.size();
    }

    /**
     * Clears all cached items from RAM.
     */
    public void clear() {
        cacheMap.clear();
    }
}
