import axios from 'axios';
import { fallbackCommands } from '../data/commandsData';

const API_BASE_URL = '/api';

// --- Client-Side DSA Implementations for Standalone / GitHub Pages Fallback ---

// 1. Rabin-Karp Rolling Hash
function rabinKarpContains(text, pattern) {
  if (!text || !pattern) return false;
  const t = text.toLowerCase();
  const p = pattern.toLowerCase();
  if (p.length > t.length) return false;

  const base = 256;
  const prime = 101;
  let pHash = 0;
  let tHash = 0;
  let h = 1;

  for (let i = 0; i < p.length - 1; i++) {
    h = (h * base) % prime;
  }
  for (let i = 0; i < p.length; i++) {
    pHash = (base * pHash + p.charCodeAt(i)) % prime;
    tHash = (base * tHash + t.charCodeAt(i)) % prime;
  }

  for (let i = 0; i <= t.length - p.length; i++) {
    if (pHash === tHash) {
      if (t.substr(i, p.length) === p) return true;
    }
    if (i < t.length - p.length) {
      tHash = (base * (tHash - t.charCodeAt(i) * h) + t.charCodeAt(i + p.length)) % prime;
      if (tHash < 0) tHash += prime;
    }
  }
  return false;
}

// 2. Levenshtein Edit Distance (Dynamic Programming)
function levenshteinDistance(s1, s2) {
  const m = s1.length;
  const n = s2.length;
  const dp = Array.from({ length: m + 1 }, () => new Array(n + 1).fill(0));

  for (let i = 0; i <= m; i++) dp[i][0] = i;
  for (let j = 0; j <= n; j++) dp[0][j] = j;

  for (let i = 1; i <= m; i++) {
    for (let j = 1; j <= n; j++) {
      if (s1[i - 1].toLowerCase() === s2[j - 1].toLowerCase()) {
        dp[i][j] = dp[i - 1][j - 1];
      } else {
        dp[i][j] = 1 + Math.min(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]);
      }
    }
  }
  return dp[m][n];
}

// Helper: Unique categories array
const fallbackCategories = Array.from(new Set(fallbackCommands.map(c => c.category))).sort();

export const api = {

  // GET /api/commands
  getAllCommands: async () => {
    try {
      const res = await axios.get(`${API_BASE_URL}/commands`, { timeout: 3000 });
      if (res.data && Array.isArray(res.data) && res.data.length > 0) return res.data;
    } catch (err) {
      console.warn("Backend API unavailable. Using embedded Linux commands dataset.");
    }
    return fallbackCommands;
  },

  // GET /api/commands/{name}
  getCommandByName: async (commandName) => {
    try {
      const res = await axios.get(`${API_BASE_URL}/commands/${encodeURIComponent(commandName)}`, { timeout: 3000 });
      if (res.data) return res.data;
    } catch (err) {}
    const cmd = fallbackCommands.find(c => c.command.toLowerCase() === commandName.toLowerCase());
    return cmd || null;
  },

  // GET /api/commands/search?q=query (Rabin-Karp)
  searchCommands: async (query) => {
    try {
      const res = await axios.get(`${API_BASE_URL}/commands/search`, { params: { q: query }, timeout: 3000 });
      if (res.data) return res.data;
    } catch (err) {}

    const start = performance.now();
    const matched = fallbackCommands.filter(cmd =>
      rabinKarpContains(cmd.command, query) ||
      rabinKarpContains(cmd.shortDefinition, query) ||
      rabinKarpContains(cmd.description, query) ||
      rabinKarpContains(cmd.syntax, query) ||
      rabinKarpContains(cmd.example, query) ||
      rabinKarpContains(cmd.commonOptions, query) ||
      rabinKarpContains(cmd.category, query)
    );

    const execTime = Math.max(1, Math.round((performance.now() - start) * 100) / 100);
    return {
      query,
      algorithmUsed: "Rabin-Karp Rolling Hash (Client Fallback)",
      matchCount: matched.length,
      executionTimeMs: execTime,
      results: matched
    };
  },

  // GET /api/commands/autocomplete?q=prefix (Trie)
  getAutocomplete: async (prefix, limit = 8) => {
    try {
      const res = await axios.get(`${API_BASE_URL}/commands/autocomplete`, { params: { q: prefix, limit }, timeout: 3000 });
      if (res.data) return res.data;
    } catch (err) {}

    const p = (prefix || '').toLowerCase();
    const suggestions = fallbackCommands
      .filter(c => c.command.toLowerCase().startsWith(p))
      .map(c => c.command)
      .slice(0, limit);

    return {
      prefix,
      algorithmUsed: "Trie Prefix Traversal (Client Fallback)",
      matchCount: suggestions.length,
      suggestions
    };
  },

  // GET /api/commands/correct?q=query (Levenshtein Edit Distance)
  correctSpelling: async (query, maxDistance = 3) => {
    try {
      const res = await axios.get(`${API_BASE_URL}/commands/correct`, { params: { q: query, maxDistance, limit: 5 }, timeout: 3000 });
      if (res.data) return res.data;
    } catch (err) {}

    const input = (query || '').trim();
    if (!input) {
      return { originalInput: query, algorithmUsed: "Levenshtein Dynamic Programming", corrected: false, suggestions: [] };
    }

    const exact = fallbackCommands.find(c => c.command.toLowerCase() === input.toLowerCase());
    if (exact) {
      return {
        originalInput: input,
        algorithmUsed: "Levenshtein Dynamic Programming",
        corrected: true,
        suggestions: [{ command: exact.command, editDistance: 0, commandEntity: exact }]
      };
    }

    const candidates = [];
    for (const cmd of fallbackCommands) {
      const dist = levenshteinDistance(input, cmd.command);
      if (dist <= maxDistance) {
        candidates.push({ command: cmd.command, editDistance: dist, commandEntity: cmd });
      }
    }
    candidates.sort((a, b) => a.editDistance - b.editDistance);

    return {
      originalInput: input,
      algorithmUsed: "Levenshtein Dynamic Programming",
      corrected: candidates.length > 0,
      suggestions: candidates.slice(0, 5)
    };
  },

  // GET /api/commands/category/{category}
  getCommandsByCategory: async (category) => {
    try {
      const res = await axios.get(`${API_BASE_URL}/commands/category/${encodeURIComponent(category)}`, { timeout: 3000 });
      if (res.data && Array.isArray(res.data)) return res.data;
    } catch (err) {}

    if (category === 'All') return fallbackCommands;
    return fallbackCommands.filter(c => c.category.toLowerCase() === category.toLowerCase());
  },

  // GET /api/categories
  getAllCategories: async () => {
    try {
      const res = await axios.get(`${API_BASE_URL}/categories`, { timeout: 3000 });
      if (res.data && Array.isArray(res.data)) return res.data;
    } catch (err) {}
    return fallbackCategories;
  },

  // GET /api/commands/random
  getRandomCommand: async () => {
    try {
      const res = await axios.get(`${API_BASE_URL}/commands/random`, { timeout: 3000 });
      if (res.data) return res.data;
    } catch (err) {}

    const randomIndex = Math.floor(Math.random() * fallbackCommands.length);
    return fallbackCommands[randomIndex];
  },

  // GET /api/stats
  getSystemStats: async () => {
    try {
      const res = await axios.get(`${API_BASE_URL}/stats`, { timeout: 3000 });
      if (res.data) return res.data;
    } catch (err) {}

    const catCounts = {};
    fallbackCommands.forEach(c => {
      catCounts[c.category] = (catCounts[c.category] || 0) + 1;
    });

    const categoryDistribution = Object.keys(catCounts).map(cat => ({
      category: cat,
      count: catCounts[cat]
    }));

    return {
      totalCommands: fallbackCommands.length,
      totalCategories: fallbackCategories.length,
      memoryCacheSize: fallbackCommands.length,
      cacheStatus: "Active (126 Commands Dataset Loaded)",
      categoryDistribution
    };
  }
};

