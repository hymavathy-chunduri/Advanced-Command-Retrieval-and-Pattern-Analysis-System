package com.dsa.linuxintelligence.algorithm;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RabinKarpSearchTest {

    @Test
    public void testPatternMatchFile() {
        String text = "Search files matching pattern in directory";
        String pattern = "file";
        List<Integer> matches = RabinKarpSearch.search(text, pattern);
        assertFalse(matches.isEmpty());
        assertEquals(7, matches.get(0));
    }

    @Test
    public void testContainsMkdir() {
        String text = "mkdir creates a new directory in filesystem";
        assertTrue(RabinKarpSearch.contains(text, "mkdir"));
    }

    @Test
    public void testNoMatchFound() {
        String text = "Lists directory contents";
        String pattern = "kubernetes";
        List<Integer> matches = RabinKarpSearch.search(text, pattern);
        assertTrue(matches.isEmpty());
    }

    @Test
    public void testEmptyQuery() {
        String text = "grep pattern matching";
        List<Integer> matches = RabinKarpSearch.search(text, "");
        assertTrue(matches.isEmpty());
    }
}
