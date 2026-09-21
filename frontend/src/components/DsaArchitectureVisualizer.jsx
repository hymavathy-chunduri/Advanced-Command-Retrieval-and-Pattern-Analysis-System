import React from 'react';
import { Database, Zap, GitBranch, Sparkles, Layers, Cpu, CheckCircle } from 'lucide-react';

export default function DsaArchitectureVisualizer({ stats }) {
  return (
    <div className="container" style={{ padding: '2rem 1.5rem 4rem' }}>
      <div style={{ textAlign: 'center', marginBottom: '3rem' }}>
        <h2 style={{ fontSize: '2.25rem', fontWeight: 800, marginBottom: '0.75rem' }}>
          Backend DSA Architecture & Flow
        </h2>
        <p style={{ color: '#94a3b8', maxWidth: '700px', margin: '0 auto' }}>
          Explore the custom Java data structures and algorithms powering lightning-fast search, autocomplete, and spelling correction without third-party search libraries.
        </p>
      </div>

      {/* Primary Storage & Cache Status Banner */}
      <div className="glass-panel" style={{ padding: '1.5rem', marginBottom: '2.5rem', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '1rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <div style={{ width: '48px', height: '48px', borderRadius: '12px', background: 'rgba(16, 185, 129, 0.15)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#10b981' }}>
            <Database size={24} />
          </div>
          <div>
            <h4 style={{ fontSize: '1.1rem', fontWeight: 700 }}>PostgreSQL Persistent Storage + RAM Index</h4>
            <p style={{ fontSize: '0.875rem', color: '#94a3b8' }}>
              Database: <strong style={{ color: '#fff' }}>linux_command_db</strong> | Table: <strong style={{ color: '#fff' }}>linux_commands</strong> ({stats?.totalCommands || 126} records)
            </p>
          </div>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#10b981', background: 'rgba(16, 185, 129, 0.1)', padding: '0.5rem 1rem', borderRadius: '20px', fontSize: '0.85rem', fontWeight: 600 }}>
          <CheckCircle size={16} /> {stats?.cacheStatus || 'Active (O(1) HashMap + Trie Index Loaded)'}
        </div>
      </div>

      {/* 4 Algorithms Grid */}
      <div className="dsa-visualizer-container">

        {/* 1. HashMap O(1) Cache */}
        <div className="dsa-card glow-box">
          <div className="dsa-card-header">
            <Zap size={22} color="#10b981" />
            <h3 className="dsa-card-title">1. HashMap Lookup</h3>
          </div>
          <span className="dsa-complexity-tag">Time: O(1) | Space: O(N)</span>
          <p style={{ fontSize: '0.9rem', color: '#94a3b8', lineHeight: '1.6', marginBottom: '1rem' }}>
            Maintains a concurrent in-memory hash table mapping normalized command strings directly to full Command entity objects. Bypasses database I/O overhead after backend initialization.
          </p>
          <div style={{ background: '#090d16', padding: '0.75rem', borderRadius: '8px', fontFamily: 'var(--font-mono)', fontSize: '0.78rem', color: '#a7f3d0' }}>
            Map&lt;String, Command&gt; cache<br/>
            put("grep", commandObj)<br/>
            get("grep") → O(1) direct return
          </div>
        </div>

        {/* 2. Trie Prefix Tree */}
        <div className="dsa-card glow-box">
          <div className="dsa-card-header">
            <GitBranch size={22} color="#06b6d4" />
            <h3 className="dsa-card-title">2. Trie Autocomplete</h3>
          </div>
          <span className="dsa-complexity-tag" style={{ color: '#38bdf8', background: 'rgba(6,182,212,0.15)' }}>Time: O(K) | Prefix Tree</span>
          <p style={{ fontSize: '0.9rem', color: '#94a3b8', lineHeight: '1.6', marginBottom: '1rem' }}>
            Custom TrieNode tree populated with all command names. Traverses down prefix nodes in O(K) time and executes Depth-First Search (DFS) to collect instantaneous suggestions.
          </p>
          <div style={{ background: '#090d16', padding: '0.75rem', borderRadius: '8px', fontFamily: 'var(--font-mono)', fontSize: '0.78rem', color: '#7dd3fc' }}>
            root → 'm' → 'k' → [isEnd=true]<br/>
            DFS traverse children:<br/>
            "mk" → ["mkdir", "mktemp", "mkfs"]
          </div>
        </div>

        {/* 3. Rabin-Karp Rolling Hash */}
        <div className="dsa-card glow-box">
          <div className="dsa-card-header">
            <Sparkles size={22} color="#a855f7" />
            <h3 className="dsa-card-title">3. Rabin-Karp Search</h3>
          </div>
          <span className="dsa-complexity-tag" style={{ color: '#c084fc', background: 'rgba(168,85,247,0.15)' }}>Time: O(N + M) | Rolling Hash</span>
          <p style={{ fontSize: '0.9rem', color: '#94a3b8', lineHeight: '1.6', marginBottom: '1rem' }}>
            Computes polynomial rolling hash values across text fields (names, descriptions, syntax, options). Updates window hash in O(1) and verifies matching characters on collision.
          </p>
          <div style={{ background: '#090d16', padding: '0.75rem', borderRadius: '8px', fontFamily: 'var(--font-mono)', fontSize: '0.78rem', color: '#e9d5ff' }}>
            H(s) = Σ(s[i] * B^(m-1-i)) % Q<br/>
            textHash = (BASE * (hash - out*h) + in) % Q<br/>
            Verify char match if hash matches
          </div>
        </div>

        {/* 4. Levenshtein Edit Distance */}
        <div className="dsa-card glow-box">
          <div className="dsa-card-header">
            <Layers size={22} color="#f59e0b" />
            <h3 className="dsa-card-title">4. Edit Distance</h3>
          </div>
          <span className="dsa-complexity-tag" style={{ color: '#fbbf24', background: 'rgba(245,158,11,0.15)' }}>Time: O(M × N) | Dynamic Prog.</span>
          <p style={{ fontSize: '0.9rem', color: '#94a3b8', lineHeight: '1.6', marginBottom: '1rem' }}>
            Fills a 2D dynamic programming matrix dp[i][j] comparing misspelled inputs against stored command names. Calculates minimum cost across insertion, deletion, and substitution.
          </p>
          <div style={{ background: '#090d16', padding: '0.75rem', borderRadius: '8px', fontFamily: 'var(--font-mono)', fontSize: '0.78rem', color: '#fde68a' }}>
            dp[i][j] = min(<br/>
            &nbsp;&nbsp;dp[i][j-1] + 1, // insert<br/>
            &nbsp;&nbsp;dp[i-1][j] + 1, // delete<br/>
            &nbsp;&nbsp;dp[i-1][j-1] + cost // replace<br/>
            )
          </div>
        </div>

      </div>
    </div>
  );
}
