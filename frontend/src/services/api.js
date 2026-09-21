import axios from 'axios';

const API_BASE_URL = '/api';

export const api = {

  // GET /api/commands
  getAllCommands: async () => {
    const res = await axios.get(`${API_BASE_URL}/commands`);
    return res.data;
  },

  // GET /api/commands/{name}
  getCommandByName: async (commandName) => {
    const res = await axios.get(`${API_BASE_URL}/commands/${encodeURIComponent(commandName)}`);
    return res.data;
  },

  // GET /api/commands/search?q=query (Rabin-Karp)
  searchCommands: async (query) => {
    const res = await axios.get(`${API_BASE_URL}/commands/search`, {
      params: { q: query }
    });
    return res.data;
  },

  // GET /api/commands/autocomplete?q=prefix (Trie)
  getAutocomplete: async (prefix, limit = 8) => {
    const res = await axios.get(`${API_BASE_URL}/commands/autocomplete`, {
      params: { q: prefix, limit }
    });
    return res.data;
  },

  // GET /api/commands/correct?q=query (Levenshtein Edit Distance)
  correctSpelling: async (query, maxDistance = 3) => {
    const res = await axios.get(`${API_BASE_URL}/commands/correct`, {
      params: { q: query, maxDistance, limit: 5 }
    });
    return res.data;
  },

  // GET /api/commands/category/{category}
  getCommandsByCategory: async (category) => {
    const res = await axios.get(`${API_BASE_URL}/commands/category/${encodeURIComponent(category)}`);
    return res.data;
  },

  // GET /api/categories
  getAllCategories: async () => {
    const res = await axios.get(`${API_BASE_URL}/categories`);
    return res.data;
  },

  // GET /api/commands/random
  getRandomCommand: async () => {
    const res = await axios.get(`${API_BASE_URL}/commands/random`);
    return res.data;
  },

  // GET /api/stats
  getSystemStats: async () => {
    const res = await axios.get(`${API_BASE_URL}/stats`);
    return res.data;
  }
};
