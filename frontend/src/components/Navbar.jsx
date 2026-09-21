import React from 'react';
import { Terminal, Cpu, BookOpen, Layers, Zap } from 'lucide-react';

export default function Navbar({ activeTab, setActiveTab, cacheStatus }) {
  return (
    <header className="navbar">
      <div className="container nav-content">
        <a href="#home" onClick={() => setActiveTab('dashboard')} className="brand">
          <div className="brand-icon">
            <Terminal size={22} />
          </div>
          <div>
            <span>Linux Command Intelligence</span>
          </div>
        </a>

        <nav>
          <ul className="nav-links">
            <li>
              <button
                className={`nav-link ${activeTab === 'dashboard' ? 'active' : ''}`}
                onClick={() => setActiveTab('dashboard')}
              >
                <Zap size={16} /> Search & Dashboard
              </button>
            </li>
            <li>
              <button
                className={`nav-link ${activeTab === 'categories' ? 'active' : ''}`}
                onClick={() => setActiveTab('categories')}
              >
                <Layers size={16} /> Category Browser
              </button>
            </li>
            <li>
              <button
                className={`nav-link ${activeTab === 'architecture' ? 'active' : ''}`}
                onClick={() => setActiveTab('architecture')}
              >
                <Cpu size={16} /> DSA Architecture
              </button>
            </li>
          </ul>
        </nav>

        <div className="dsa-badge">
          <Cpu size={14} />
          <span>DSA BACKEND ACTIVE</span>
        </div>
      </div>
    </header>
  );
}
