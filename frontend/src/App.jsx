import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import CategoryBrowser from './pages/CategoryBrowser';
import DsaArchitectureVisualizer from './components/DsaArchitectureVisualizer';
import { api } from './services/api';

export default function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [stats, setStats] = useState(null);

  useEffect(() => {
    api.getSystemStats().then(setStats).catch(() => {});
  }, []);

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <Navbar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        cacheStatus={stats?.cacheStatus}
      />

      <main style={{ flex: 1 }}>
        {activeTab === 'dashboard' && <Dashboard setActiveTab={setActiveTab} />}
        {activeTab === 'categories' && <CategoryBrowser />}
        {activeTab === 'architecture' && <DsaArchitectureVisualizer stats={stats} />}
      </main>

      <footer style={{ borderTop: '1px solid rgba(255,255,255,0.08)', padding: '1.5rem 0', textAlign: 'center', fontSize: '0.85rem', color: '#64748b' }}>
        <div className="container">
          <p>
            <strong>Linux Command Intelligence</strong> — Academic Data Structures & Algorithms Project
          </p>
          <p style={{ marginTop: '0.25rem', fontSize: '0.78rem' }}>
            Powered by Spring Boot, PostgreSQL, React, Vite, and custom Java Rabin-Karp, Trie, Levenshtein DP & HashMap algorithms.
          </p>
        </div>
      </footer>
    </div>
  );
}
