import React, { useState, useEffect } from 'react';
import { api } from '../services/api';
import CommandCard from '../components/CommandCard';
import CommandModal from '../components/CommandModal';
import { Layers, Filter } from 'lucide-react';

export default function CategoryBrowser() {
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [commands, setCommands] = useState([]);
  const [selectedCommand, setSelectedCommand] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadCategories();
    loadCategoryCommands('All');
  }, []);

  const loadCategories = async () => {
    try {
      const data = await api.getAllCategories();
      setCategories(['All', ...data]);
    } catch (err) {}
  };

  const loadCategoryCommands = async (catName) => {
    setSelectedCategory(catName);
    setLoading(true);
    try {
      if (catName === 'All') {
        const data = await api.getAllCommands();
        setCommands(data);
      } else {
        const data = await api.getCommandsByCategory(catName);
        setCommands(data);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container" style={{ padding: '2rem 1.5rem 4rem' }}>
      <div style={{ textCenter: 'center', marginBottom: '2.5rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '0.5rem' }}>
          <Layers size={28} color="#10b981" />
          <h2 style={{ fontSize: '2rem', fontWeight: 800 }}>Browse Commands by Category</h2>
        </div>
        <p style={{ color: '#94a3b8' }}>Explore structured Linux utilities organized across 23 domain areas.</p>
      </div>

      {/* Category Chips Selector */}
      <div className="category-chips-grid">
        {categories.map((cat) => (
          <button
            key={cat}
            className={`category-chip ${selectedCategory === cat ? 'active' : ''}`}
            onClick={() => loadCategoryCommands(cat)}
          >
            {cat}
          </button>
        ))}
      </div>

      {/* Results Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.25rem' }}>
        <h3 style={{ fontSize: '1.25rem', fontWeight: 700, color: '#f8fafc' }}>
          Category: <span style={{ color: '#10b981' }}>{selectedCategory}</span>
        </h3>
        <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>
          Showing {commands.length} command(s)
        </span>
      </div>

      {/* Cards Grid */}
      {loading ? (
        <div style={{ textAlign: 'center', padding: '4rem 0', color: '#94a3b8' }}>Loading category items...</div>
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

      {selectedCommand && (
        <CommandModal
          command={selectedCommand}
          onClose={() => setSelectedCommand(null)}
        />
      )}
    </div>
  );
}
