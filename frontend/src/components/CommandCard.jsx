import React from 'react';
import { Shield, ShieldAlert, ShieldCheck, ChevronRight, Terminal } from 'lucide-react';

export default function CommandCard({ command, onClick }) {
  const getSafetyBadge = (level) => {
    const lvl = (level || 'safe').toLowerCase();
    if (lvl === 'dangerous') {
      return (
        <span className="safety-badge dangerous">
          <ShieldAlert size={14} /> Dangerous
        </span>
      );
    } else if (lvl === 'caution') {
      return (
        <span className="safety-badge caution">
          <Shield size={14} /> Caution
        </span>
      );
    }
    return (
      <span className="safety-badge safe">
        <ShieldCheck size={14} /> Safe
      </span>
    );
  };

  return (
    <div className="glass-panel glow-box command-card" onClick={onClick}>
      <div>
        <div className="card-top">
          <span className="cmd-title">{command.command}</span>
          <span className="category-tag">{command.category}</span>
        </div>
        <p className="cmd-short-def">{command.shortDefinition}</p>
        <div className="cmd-syntax-preview">
          <code>$ {command.syntax}</code>
        </div>
      </div>

      <div className="card-footer">
        {getSafetyBadge(command.safetyLevel)}
        <span style={{ color: '#10b981', fontSize: '0.85rem', fontWeight: 600, display: 'flex', alignItems: 'center' }}>
          Details <ChevronRight size={14} />
        </span>
      </div>
    </div>
  );
}
