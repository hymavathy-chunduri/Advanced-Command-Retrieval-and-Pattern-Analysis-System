import React, { useState } from 'react';
import { X, Copy, Check, ShieldCheck, ShieldAlert, Shield, Terminal, Info, Code2 } from 'lucide-react';

export default function CommandModal({ command, onClose, onSelectRelated }) {
  const [copied, setCopied] = useState(false);

  if (!command) return null;

  const copyToClipboard = (text) => {
    navigator.clipboard.writeText(text);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const getSafetyBadge = (level) => {
    const lvl = (level || 'safe').toLowerCase();
    if (lvl === 'dangerous') {
      return <span className="safety-badge dangerous"><ShieldAlert size={16} /> Dangerous (High Privilege / Data Risk)</span>;
    } else if (lvl === 'caution') {
      return <span className="safety-badge caution"><Shield size={16} /> Caution (System State Modification)</span>;
    }
    return <span className="safety-badge safe"><ShieldCheck size={16} /> Safe (Read-Only / Safe Execution)</span>;
  };

  // Format common options split by newline
  const optionsList = command.commonOptions
    ? command.commonOptions.split('\n').filter(opt => opt.trim().length > 0)
    : [];

  // Format related commands
  const relatedList = command.relatedCommands
    ? command.relatedCommands.split(',').map(rc => rc.trim()).filter(Boolean)
    : [];

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <button className="modal-close-btn" onClick={onClose}>
          <X size={20} />
        </button>

        <div className="detail-header">
          <div className="detail-title-row">
            <h2 className="detail-cmd-name">{command.command}</h2>
            <span className="category-tag" style={{ fontSize: '0.85rem' }}>{command.category}</span>
          </div>
          <p style={{ color: '#94a3b8', fontSize: '1.05rem' }}>{command.shortDefinition}</p>
        </div>

        {/* Syntax Block */}
        <div>
          <span className="section-title">Syntax</span>
          <div className="code-block-wrapper">
            <code>{command.syntax}</code>
            <button className="copy-btn" onClick={() => copyToClipboard(command.syntax)}>
              {copied ? <Check size={14} /> : <Copy size={14} />}
              {copied ? 'Copied' : 'Copy Syntax'}
            </button>
          </div>
        </div>

        {/* Description */}
        <div style={{ marginBottom: '1.5rem' }}>
          <span className="section-title">Description</span>
          <p style={{ color: '#cbd5e1', lineHeight: '1.7' }}>{command.description}</p>
        </div>

        {/* Example */}
        <div style={{ marginBottom: '1.5rem' }}>
          <span className="section-title">Practical Example</span>
          <div className="code-block-wrapper" style={{ borderColor: 'rgba(6, 182, 212, 0.3)' }}>
            <code>$ {command.example}</code>
            <button className="copy-btn" onClick={() => copyToClipboard(command.example)}>
              {copied ? <Check size={14} /> : <Copy size={14} />}
              {copied ? 'Copied' : 'Copy Example'}
            </button>
          </div>
          <p style={{ fontSize: '0.9rem', color: '#94a3b8', fontStyle: 'italic', marginTop: '0.4rem' }}>
            💡 {command.exampleExplanation}
          </p>
        </div>

        {/* Common Options */}
        {optionsList.length > 0 && (
          <div style={{ marginBottom: '1.5rem' }}>
            <span className="section-title">Common Options</span>
            <div style={{ background: '#090d16', borderRadius: '8px', padding: '0.75rem 1rem', border: '1px solid rgba(255,255,255,0.05)' }}>
              {optionsList.map((opt, idx) => (
                <div key={idx} style={{ fontFamily: 'var(--font-mono)', fontSize: '0.85rem', color: '#cbd5e1', padding: '0.35rem 0', borderBottom: idx < optionsList.length - 1 ? '1px solid rgba(255,255,255,0.04)' : 'none' }}>
                  {opt}
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Related Commands */}
        {relatedList.length > 0 && (
          <div style={{ marginBottom: '1.5rem' }}>
            <span className="section-title">Related Commands</span>
            <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap', marginTop: '0.4rem' }}>
              {relatedList.map((relCmd) => (
                <button
                  key={relCmd}
                  className="spelling-suggestion-chip"
                  onClick={() => onSelectRelated && onSelectRelated(relCmd)}
                >
                  {relCmd}
                </button>
              ))}
            </div>
          </div>
        )}

        {/* Safety & Distribution */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '1rem', borderTop: '1px solid rgba(255,255,255,0.08)' }}>
          <div>
            <span className="section-title" style={{ display: 'block', marginBottom: '0.25rem' }}>Safety Assessment</span>
            {getSafetyBadge(command.safetyLevel)}
          </div>
          <div style={{ textAlign: 'right' }}>
            <span className="section-title" style={{ display: 'block', marginBottom: '0.25rem' }}>Target Distribution</span>
            <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>{command.distribution}</span>
          </div>
        </div>
      </div>
    </div>
  );
}
