import React from 'react';
import { History, Trash2, Clock } from 'lucide-react';

export default function HistoryPanel({ history, onSelectQuery, onClearHistory }) {
  if (!history || history.length === 0) return null;

  return (
    <div style={{ marginBottom: '2rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.75rem' }}>
        <span className="section-title" style={{ display: 'flex', alignItems: 'center', gap: '0.4rem', margin: 0 }}>
          <History size={14} /> Recent Search History
        </span>
        <button
          onClick={onClearHistory}
          style={{ background: 'none', border: 'none', color: '#64748b', fontSize: '0.8rem', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '0.2rem' }}
        >
          <Trash2 size={12} /> Clear History
        </button>
      </div>

      <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
        {history.map((item, idx) => (
          <button
            key={idx}
            className="category-chip"
            style={{ padding: '0.35rem 0.75rem', fontSize: '0.825rem', fontFamily: 'var(--font-mono)' }}
            onClick={() => onSelectQuery(item)}
          >
            <Clock size={12} style={{ display: 'inline', marginRight: '0.3rem' }} />
            {item}
          </button>
        ))}
      </div>
    </div>
  );
}
