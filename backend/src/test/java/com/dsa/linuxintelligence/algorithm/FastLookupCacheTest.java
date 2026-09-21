package com.dsa.linuxintelligence.algorithm;

import com.dsa.linuxintelligence.model.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FastLookupCacheTest {

    private FastLookupCache cache;

    @BeforeEach
    public void setUp() {
        cache = new FastLookupCache();
        Command grep = new Command(1L, "grep", "Text Processing", "Search text",
                "Description", "syntax", "example", "explanation", "options", "sed", "safe", "All");
        cache.put(grep);
    }

    @Test
    public void testO1RetrievalGrep() {
        Command cmd = cache.get("grep");
        assertNotNull(cmd);
        assertEquals("grep", cmd.getCommand());
        assertEquals("Text Processing", cmd.getCategory());
    }

    @Test
    public void testCaseInsensitiveRetrieval() {
        Command cmd = cache.get("GREP");
        assertNotNull(cmd);
        assertEquals("grep", cmd.getCommand());
    }

    @Test
    public void testUnknownCommand() {
        Command cmd = cache.get("nonexistentcommand");
        assertNull(cmd);
    }
}
