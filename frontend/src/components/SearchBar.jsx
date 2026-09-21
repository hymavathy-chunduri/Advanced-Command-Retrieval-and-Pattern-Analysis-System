import React, { useState, useEffect, useRef } from 'react';
import { Search, X, Sparkles, ArrowRight } from 'lucide-react';
import { api } from '../services/api';

export default function SearchBar({ query, setQuery, onSearchSubmit, onSelectSuggestion }) {
  const [suggestions, setSuggestions] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(-1);
  const dropdownRef = useRef(null);

  // Real-time Trie Autocomplete fetch on query change
  useEffect(() => {
    if (!query || query.trim().length === 0) {
      setSuggestions([]);
      setShowDropdown(false);
      return;
    }

    const timer = setTimeout(async () => {
      try {
        const data = await api.getAutocomplete(query.trim(), 8);
        if (data && data.suggestions) {
          setSuggestions(data.suggestions);
          setShowDropdown(data.suggestions.length > 0);
        }
      } catch (err) {
        console.error("Trie autocomplete fetch error:", err);
      }
    }, 120);

    return () => clearTimeout(timer);
  }, [query]);

  // Click outside to close dropdown
  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleKeyDown = (e) => {
    if (e.key === 'ArrowDown') {
      e.preventDefault();
      setSelectedIndex((prev) => (prev < suggestions.length - 1 ? prev + 1 : 0));
    } else if (e.key === 'ArrowUp') {
      e.preventDefault();
      setSelectedIndex((prev) => (prev > 0 ? prev - 1 : suggestions.length - 1));
    } else if (e.key === 'Enter') {
      e.preventDefault();
      if (selectedIndex >= 0 && suggestions[selectedIndex]) {
        handleSuggestionClick(suggestions[selectedIndex]);
      } else {
        setShowDropdown(false);
        onSearchSubmit(query);
      }
    } else if (e.key === 'Escape') {
      setShowDropdown(false);
    }
  };

  const handleSuggestionClick = (cmdName) => {
    setQuery(cmdName);
    setShowDropdown(false);
    onSelectSuggestion(cmdName);
  };

  return (
    <div className="search-container" ref={dropdownRef}>
      <div className="search-input-wrapper">
        <Search className="search-icon" />
        <input
          type="text"
          className="search-input"
          placeholder="Search commands, patterns, or descriptions (e.g. grep, file, mk)..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onKeyDown={handleKeyDown}
          onFocus={() => query.trim().length > 0 && suggestions.length > 0 && setShowDropdown(true)}
        />
        {query && (
          <button className="search-clear-btn" onClick={() => { setQuery(''); setSuggestions([]); setShowDropdown(false); }}>
            <X size={18} />
          </button>
        )}
      </div>

      {showDropdown && suggestions.length > 0 && (
        <div className="autocomplete-dropdown">
          <div className="autocomplete-header">
            <span>TRIE PREFIX AUTOCOMPLETE</span>
            <span>O(K) Complexity</span>
          </div>
          {suggestions.map((cmd, idx) => (
            <div
              key={cmd}
              className={`autocomplete-item ${idx === selectedIndex ? 'active' : ''}`}
              onClick={() => handleSuggestionClick(cmd)}
            >
              <span className="autocomplete-cmd">{cmd}</span>
              <span style={{ fontSize: '0.75rem', color: '#64748b' }}>
                Quick Jump <ArrowRight size={12} style={{ display: 'inline' }} />
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
