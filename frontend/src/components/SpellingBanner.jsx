import React from 'react';
import { HelpCircle, ArrowRight } from 'lucide-react';

export default function SpellingBanner({ originalInput, suggestions, onSelectSuggestion }) {
  if (!suggestions || suggestions.length === 0) return null;

  return (
    <div className="spelling-banner">
      <div className="spelling-text">
        <HelpCircle size={20} color="#f59e0b" />
        <div>
          <span>Did you mean </span>
          <strong style={{ color: '#fff' }}>"{suggestions[0].command}"</strong>
          <span style={{ fontSize: '0.85rem', color: '#94a3b8', marginLeft: '0.5rem' }}>
            (Levenshtein Edit Distance: {suggestions[0].editDistance})
          </span>
        </div>
      </div>

      <div style={{ display: 'flex', gap: '0.5rem' }}>
        {suggestions.slice(0, 3).map((sugg) => (
          <button
            key={sugg.command}
            className="spelling-suggestion-chip"
            onClick={() => onSelectSuggestion(sugg.command)}
          >
            {sugg.command}
          </button>
        ))}
      </div>
    </div>
  );
}
