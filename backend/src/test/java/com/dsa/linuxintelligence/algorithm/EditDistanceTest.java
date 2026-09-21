package com.dsa.linuxintelligence.algorithm;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditDistanceTest {

    @Test
    public void testDistanceGrpeToGrep() {
        int dist = EditDistance.compute("grpe", "grep");
        // "grpe" -> swap p and e = substitution/transposition edit distance 2
        assertTrue(dist == 1 || dist == 2);
    }

    @Test
    public void testExactMatch() {
        assertEquals(0, EditDistance.compute("grep", "grep"));
    }

    @Test
    public void testFindBestMatches() {
        List<String> dictionary = Arrays.asList("grep", "groupadd", "groups", "find", "mkdir");
        List<EditDistance.CorrectionCandidate> candidates = EditDistance.findBestMatches("grpe", dictionary, 3, 5);

        assertFalse(candidates.isEmpty());
        assertEquals("grep", candidates.get(0).getCommand());
    }
}
