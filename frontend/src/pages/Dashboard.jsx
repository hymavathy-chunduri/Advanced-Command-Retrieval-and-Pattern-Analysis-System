import React, { useState, useEffect } from 'react';
import SearchBar from '../components/SearchBar';
import CommandCard from '../components/CommandCard';
import CommandModal from '../components/CommandModal';
import SpellingBanner from '../components/SpellingBanner';
import HistoryPanel from '../components/HistoryPanel';
import { api } from '../services/api';
import { Search, Sparkles, Wand2, Grid, Shuffle, Database, Code, Terminal, Clock } from 'lucide-react';

export default function Dashboard({ setActiveTab }) {
  const [query, setQuery] = useState('');
  const [commands, setCommands] = useState([]);
  const [selectedCommand, setSelectedCommand] = useState(null);
  const [spellingSuggestions, setSpellingSuggestions] = useState([]);
  const [history, setHistory] = useState([]);
  const [randomCommand, setRandomCommand] = useState(null);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);
  const [dsaMetrics, setDsaMetrics] = useState({ algorithm: 'All Commands', count: 0, timeMs: 0 });

  // Load initial dataset, stats, and local search history
  useEffect(() => {
    loadAllCommands();
    loadSystemStats();
    loadRandomCommand();

    const savedHistory = localStorage.getItem('linux_cmd_history');
    if (savedHistory) {
      try { setHistory(JSON.parse(savedHistory)); } catch (e) {}
    }
  }, []);

  const loadAllCommands = async () => {
    setLoading(true);
    try {
      const data = await api.getAllCommands();
      setCommands(data);
      setDsaMetrics({ algorithm: 'PostgreSQL Database Load', count: data.length, timeMs: 0 });
    } catch (err) {
      console.error("Failed to fetch commands:", err);
    } finally {
      setLoading(false);
    }
  };

  const loadSystemStats = async () => {
    try {
      const data = await api.getSystemStats();
      setStats(data);
    } catch (err) {}
  };

  const loadRandomCommand = async () => {
    try {
      const data = await api.getRandomCommand();
      setRandomCommand(data);
    } catch (err) {}
  };

  const saveToHistory = (searchQuery) => {
    if (!searchQuery || searchQuery.trim().length === 0) return;
    const trimmed = searchQuery.trim();
    setHistory((prev) => {
      const filtered = prev.filter((item) => item.toLowerCase() !== trimmed.toLowerCase());
      const updated = [trimmed, ...filtered].slice(0, 10);
      localStorage.setItem('linux_cmd_history', JSON.stringify(updated));
      return updated;
    });
  };

  // Perform Rabin-Karp Pattern Search
  const handlePatternSearch = async (searchTerm = query) => {
    if (!searchTerm || searchTerm.trim().length === 0) {
      loadAllCommands();
      setSpellingSuggestions([]);
      return;
    }

    setLoading(true);
    saveToHistory(searchTerm);

    try {
      // 1. Try Rabin-Karp Pattern Search
      const searchRes = await api.searchCommands(searchTerm);
      
      if (searchRes && searchRes.results && searchRes.results.length > 0) {
        setCommands(searchRes.results);
        setSpellingSuggestions([]);
        setDsaMetrics({
          algorithm: searchRes.algorithmUsed,
          count: searchRes.matchCount,
          timeMs: searchRes.executionTimeMs
        });
      } else {
        // 2. Fallback to Levenshtein Edit Distance Spelling Correction if pattern match yields 0
        const correctRes = await api.correctSpelling(searchTerm, 3);
        setCommands([]);
        if (correctRes && correctRes.suggestions && correctRes.suggestions.length > 0) {
          setSpellingSuggestions(correctRes.suggestions);
          setDsaMetrics({
            algorithm: correctRes.algorithmUsed + " (Typo Detected)",
            count: correctRes.suggestions.length,
            timeMs: 0
          });
        } else {
          setSpellingSuggestions([]);
          setDsaMetrics({ algorithm: 'Rabin-Karp Pattern Search', count: 0, timeMs: 0 });
        }
      }
    } catch (err) {
      console.error("Search error:", err);
    } finally {
      setLoading(false);
    }
  };

  // Select exact command via Trie or Suggestion
  const handleSelectSuggestion = async (cmdName) => {
    setQuery(cmdName);
    saveToHistory(cmdName);
    setLoading(true);

    try {
      const cmdData = await api.getCommandByName(cmdName);
      if (cmdData) {
        setSelectedCommand(cmdData);
        setCommands([cmdData]);
        setSpellingSuggestions([]);
        setDsaMetrics({ algorithm: 'HashMap Fast O(1) Lookup', count: 1, timeMs: 0 });
      } else {
        handlePatternSearch(cmdName);
      }
    } catch (err) {
      handlePatternSearch(cmdName);
    } finally {
      setLoading(false);
    }
  };

  // Handle spelling suggestion pick
  const handlePickSpellingSuggestion = (suggestedCmd) => {
    setQuery(suggestedCmd);
    handleSelectSuggestion(suggestedCmd);
  };

  return (
    <div className="container" style={{ paddingBottom: '4rem' }}>
      
      {/* Hero Header */}
      <div className="hero-section">
        <h1 className="hero-title">Linux Command Intelligence</h1>
        <p className="hero-subtitle">
          Advanced Command Retrieval and Pattern Analysis System powered by manual Java DSA algorithms: Rabin-Karp, Trie, Levenshtein Edit Distance, and HashMap Cache.
        </p>

        {/* Central Search Bar */}
        <SearchBar
          query={query}
          setQuery={setQuery}
          onSearchSubmit={(q) => handlePatternSearch(q)}
          onSelectSuggestion={handleSelectSuggestion}
        />

        {/* Quick Action Mode Buttons */}
        <div className="quick-actions">
          <button className="action-btn primary" onClick={() => handlePatternSearch(query)}>
            <Search size={16} /> [Search] Rabin-Karp
          </button>
          <button className="action-btn" onClick={() => handleSelectSuggestion(query || 'mkdir')}>
            <Sparkles size={16} /> [Autocomplete] Trie
          </button>
          <button className="action-btn" onClick={async () => {
            const badInput = query || 'grpe';
            setQuery(badInput);
            const data = await api.correctSpelling(badInput, 3);
            if (data && data.suggestions) setSpellingSuggestions(data.suggestions);
          }}>
            <Wand2 size={16} /> [Fix Spelling] Edit Distance
          </button>
          <button className="action-btn" onClick={() => setActiveTab('categories')}>
            <Grid size={16} /> [Browse Commands]
          </button>
        </div>
      </div>

      {/* Did You Mean Spelling Banner */}
      <SpellingBanner
        originalInput={query}
        suggestions={spellingSuggestions}
        onSelectSuggestion={handlePickSpellingSuggestion}
      />

      {/* Dashboard Stats */}
      {stats && (
        <div className="stats-grid">
          <div className="glass-panel stat-card">
            <div className="stat-icon">
              <Database size={24} />
            </div>
            <div>
              <div className="stat-value">{stats.totalCommands}</div>
              <div className="stat-label">Total Stored Commands</div>
            </div>
          </div>

          <div className="glass-panel stat-card">
            <div className="stat-icon" style={{ background: 'rgba(6, 182, 212, 0.15)', color: '#06b6d4' }}>
              <Grid size={24} />
            </div>
            <div>
              <div className="stat-value">{stats.totalCategories}</div>
              <div className="stat-label">Command Categories</div>
            </div>
          </div>

          <div className="glass-panel stat-card">
            <div className="stat-icon" style={{ background: 'rgba(168, 85, 247, 0.15)', color: '#a855f7' }}>
              <Terminal size={24} />
            </div>
            <div>
              <div className="stat-value">{stats.memoryCacheSize}</div>
              <div className="stat-label">RAM HashMap Cached</div>
            </div>
          </div>

          {/* Random Command Card Widget */}
          {randomCommand && (
            <div
              className="glass-panel stat-card glow-box"
              style={{ cursor: 'pointer', borderColor: 'rgba(16, 185, 129, 0.3)' }}
              onClick={() => setSelectedCommand(randomCommand)}
            >
              <div className="stat-icon" style={{ background: 'rgba(245, 158, 11, 0.15)', color: '#f59e0b' }}>
                <Shuffle size={24} />
              </div>
              <div style={{ overflow: 'hidden' }}>
                <div style={{ fontSize: '0.75rem', color: '#f59e0b', fontWeight: 700 }}>RANDOM COMMAND</div>
                <div className="stat-value" style={{ fontSize: '1.25rem', fontFamily: 'var(--font-mono)', color: '#38bdf8' }}>
                  {randomCommand.command}
                </div>
              </div>
            </div>
          )}
        </div>
      )}

      {/* Recent Search History */}
      <HistoryPanel
        history={history}
        onSelectQuery={(hQuery) => { setQuery(hQuery); handlePatternSearch(hQuery); }}
        onClearHistory={() => { setHistory([]); localStorage.removeItem('linux_cmd_history'); }}
      />

      {/* Active Search & Algorithm Metric Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.25rem' }}>
        <h3 style={{ fontSize: '1.25rem', fontWeight: 700 }}>
          {query ? `Search Results for "${query}"` : 'All Commands Catalog'}
        </h3>
        <span style={{ fontSize: '0.85rem', color: '#34d399', background: 'rgba(16, 185, 129, 0.1)', padding: '0.3rem 0.75rem', borderRadius: '20px', fontFamily: 'var(--font-mono)' }}>
          {dsaMetrics.algorithm} | {dsaMetrics.count} match(es) {dsaMetrics.timeMs > 0 ? `(${dsaMetrics.timeMs}ms)` : ''}
        </span>
      </div>

      {/* Commands Results Grid */}
      {loading ? (
        <div style={{ textAlign: 'center', padding: '4rem 0', color: '#94a3b8' }}>
          <div style={{ fontSize: '1.2rem', marginBottom: '0.5rem' }}>⚡ Executing DSA Search Engine...</div>
        </div>
      ) : commands.length === 0 ? (
        <div className="glass-panel" style={{ textCenter: 'center', padding: '3rem', textAlign: 'center' }}>
          <p style={{ fontSize: '1.1rem', color: '#cbd5e1', marginBottom: '0.5rem' }}>No exact commands or patterns found for "{query}".</p>
          <p style={{ color: '#64748b', fontSize: '0.9rem' }}>Try using [Fix Spelling] or search by partial keyword.</p>
        </div>
      ) : (
        <div className="cards-grid">
          {commands.map((cmd) => (
            <CommandCard
              key={cmd.id || cmd.command}
              command={cmd}
              onClick={() => setSelectedCommand(cmd)}
            />
          ))}
        </div>
      )}

      {/* Detail View Modal */}
      {selectedCommand && (
        <CommandModal
          command={selectedCommand}
          onClose={() => setSelectedCommand(null)}
          onSelectRelated={(relCmd) => {
            setSelectedCommand(null);
            handleSelectSuggestion(relCmd);
          }}
        />
      )}
    </div>
  );
}
