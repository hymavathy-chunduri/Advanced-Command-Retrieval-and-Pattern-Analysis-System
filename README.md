# Linux Command Intelligence
### Advanced Command Retrieval and Pattern Analysis System
*An Academic Data Structures and Algorithms (DSA) Web Application*

---

## 1. Project Overview

**Linux Command Intelligence** is a full-stack academic DSA web application engineered to enable fast, intelligent searching, pattern matching, autocomplete, and typo correction for Linux commands.

The project demonstrates how manual algorithm design (written from scratch in pure Java without third-party search libraries) can be combined with persistent PostgreSQL storage, Spring Boot REST APIs, and a modern React frontend dashboard.

---

## 2. Key Features

- **Exact & Fast Command Retrieval**: Instant $O(1)$ command lookup using a custom in-memory `HashMap` cache.
- **Pattern Search**: Multi-field pattern matching across command names, short definitions, syntax, descriptions, and examples powered by a manual **Rabin-Karp Rolling Hash** algorithm.
- **Prefix Autocomplete**: Instant $O(K)$ prefix suggestion matching powered by a manual **Trie** data structure.
- **Spelling Correction**: Intelligent typo detection and nearest match suggestion powered by a manual **Levenshtein Edit Distance (Dynamic Programming)** algorithm.
- **Interactive Command Detail Modal**: Shows command syntax, practical examples, copy-to-clipboard functionality, common options, related command shortcuts, target distributions, and safety level assessments.
- **Category Browser**: Browse commands organized into 23 domain areas (e.g. Navigation, Text Processing, Networking, Security, Processes).
- **Search History**: Client-side recent search tracking stored in `localStorage`.
- **DSA Architecture Visualizer**: Interactive UI dashboard detailing runtime metrics and algorithmic complexities.

---

## 3. Technology Stack

- **Frontend**: React 18, Vite 5, Vanilla CSS (Cyber/Terminal Dark Glassmorphism design system), Lucide React Icons.
- **Backend**: Java 17/27, Spring Boot 3.2, Spring Data JPA, Maven.
- **Database**: PostgreSQL 16 (Database name: `linux_command_db`, Table: `linux_commands`).
- **DSA Core**: Custom Java implementations of **Rabin-Karp**, **Trie**, **Levenshtein Edit Distance**, and **FastLookupCache (HashMap)**.

---

## 4. System Architecture

```
                       +-----------------------------+
                       | React + Vite Frontend (UI)  |
                       +--------------+--------------+
                                      |
                                      | HTTP REST Requests
                                      v
                       +--------------+--------------+
                       |  Spring Boot Backend API    |
                       +--------------+--------------+
                                      |
      +-------------------------------+-------------------------------+
      |                               |                               |
      v                               v                               v
+-----+-----+                   +-----+-----+                   +-----+-----+
|  HashMap  |                   |   Trie    |                   |Rabin-Karp |
| Cache O(1)|                   |  Index    |                   |Pattern Search
+-----+-----+                   +-----+-----+                   +-----+-----+
      |                               |                               |
      +-------------------------------+-------------------------------+
                                      |
                                      v
                       +--------------+--------------+
                       |  PostgreSQL Database (DB)   |
                       |  Table: linux_commands      |
                       +-----------------------------+
```

1. **PostgreSQL**: Permanent persistent source of truth storage.
2. **HashMap Cache**: In-memory $O(1)$ fast command lookup layer pre-loaded on backend startup.
3. **Trie Index**: Prefix tree for $O(K)$ prefix autocomplete suggestions.
4. **Rabin-Karp**: Rolling hash algorithm searching substring patterns across metadata fields in $O(N + M)$ average time.
5. **Edit Distance**: Dynamic programming $O(M \times N)$ algorithm calculating distance matrix for spelling correction when typos occur.

---

## 5. How the DSA Algorithms Work

### 5.1 Rabin-Karp Rolling Hash (`RabinKarpSearch.java`)
- **Pattern Hash**: Computes polynomial hash $H(P) = \sum (P[i] \cdot B^{M-1-i}) \pmod Q$ for pattern string of length $M$.
- **Rolling Hash**: Slides a window over text of length $N$. Updates sliding hash in $O(1)$ time:
  $$H_{next} = (B \cdot (H_{curr} - text[i] \cdot B^{M-1}) + text[i+M]) \pmod Q$$
- **Character Verification**: When hashes match ($H_{pattern} == H_{text}$), performs explicit character comparison to eliminate false positives caused by hash collisions.

### 5.2 Trie Prefix Autocomplete (`Trie.java`)
- **Trie Node (`TrieNode`)**: Contains a child map `Map<Character, TrieNode>`, `isEndOfWord` boolean flag, and stored `commandName`.
- **Insertion**: Iterates character-by-character from root down, instantiating missing child nodes.
- **Prefix Traversal**: Navigates down prefix path in $O(K)$ time (where $K$ is prefix length).
- **Suggestion Collection**: Executes Depth-First Search (DFS) starting from the prefix endpoint node to gather valid matching command names.

### 5.3 Levenshtein Edit Distance (`EditDistance.java`)
- **Dynamic Programming Matrix**: Constructs matrix `dp[i][j]` representing minimum edit operations between input string $s_1$ (length $M$) and target command $s_2$ (length $N$).
- **Recurrence Relation**:
  $$dp[i][j] = \min \begin{cases} dp[i-1][j-1] & \text{if } s_1[i-1] == s_2[j-1] \\ 1 + \min(dp[i][j-1], dp[i-1][j], dp[i-1][j-1]) & \text{otherwise} \end{cases}$$
- **Operations**: Tracks Insertion ($dp[i][j-1]$), Deletion ($dp[i-1][j]$), and Replacement ($dp[i-1][j-1]$).

### 5.4 HashMap Fast Lookup Cache (`FastLookupCache.java`)
- Maps command name keys (lowercase `String`) to complete `Command` entity objects.
- Provides average $O(1)$ retrieval bypassing database disk I/O for direct queries.

---

## 6. Database Schema & Dataset

### Schema (`linux_commands`)
```sql
CREATE TABLE linux_commands (
    id BIGSERIAL PRIMARY KEY,
    command VARCHAR(100) UNIQUE NOT NULL,
    category VARCHAR(100) NOT NULL,
    short_definition TEXT NOT NULL,
    description TEXT NOT NULL,
    syntax TEXT NOT NULL,
    example TEXT NOT NULL,
    example_explanation TEXT NOT NULL,
    common_options TEXT NOT NULL,
    related_commands TEXT NOT NULL,
    safety_level VARCHAR(30) DEFAULT 'safe',
    distribution VARCHAR(100) DEFAULT 'All Major Linux Distributions',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Dataset
The seed dataset (`database/seed.sql` and `database/linux_commands.csv`) contains **126 real Linux commands** across 23 categories:
`Navigation`, `File Management`, `Text Processing`, `File Search`, `Command Discovery`, `Shell`, `Environment`, `Processes`, `Storage`, `Networking`, `Remote Access`, `Archives`, `Permissions`, `System Information`, `System Services`, `Users`, `Package Management`, `Development`, `Editors`, `Security`, `Checksums`, `Date and Time`.

---

## 7. REST API Endpoints

| Endpoint | Method | Description | Primary DSA Algorithm |
| :--- | :--- | :--- | :--- |
| `/api/commands` | `GET` | Returns all stored commands | PostgreSQL / JPA |
| `/api/commands/{name}` | `GET` | Retrieve exact command details | **HashMap Cache ($O(1)$)** |
| `/api/commands/search?q={query}` | `GET` | Pattern search across metadata | **Rabin-Karp Rolling Hash** |
| `/api/commands/autocomplete?q={prefix}` | `GET` | Prefix suggestions | **Trie Traversal ($O(K)$)** |
| `/api/commands/correct?q={query}` | `GET` | Typo spelling correction | **Levenshtein Edit Distance** |
| `/api/commands/category/{category}` | `GET` | Get commands by category | PostgreSQL Query |
| `/api/categories` | `GET` | List all unique categories | PostgreSQL Query |
| `/api/commands/random` | `GET` | Return random command | Native DB Random |
| `/api/stats` | `GET` | System stats and category distribution | Aggregate Engine |

---

## 8. Setup & Execution Instructions

### Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+
- Node.js 18+ & npm
- PostgreSQL 16+

### Step 1: Database Setup
```bash
# Start PostgreSQL server
brew services start postgresql@16

# Create Database & User
createdb -p 5433 linux_command_db
psql -p 5433 -d linux_command_db -c "CREATE USER postgres WITH SUPERUSER PASSWORD 'postgres';"

# Load Schema & Seed Data
psql -p 5433 -d linux_command_db -f database/schema.sql
psql -p 5433 -d linux_command_db -f database/seed.sql
```

### Step 2: Backend Setup (Spring Boot)
```bash
cd backend

# Build and run tests
mvn clean test

# Start Backend Server (runs on http://localhost:8080)
mvn spring-boot:run
```

### Step 3: Frontend Setup (React + Vite)
```bash
cd frontend

# Install dependencies
npm install

# Start Vite Development Server (runs on http://localhost:5173)
npm run dev
```

---

## 9. Verification & Sample API Requests

### 1. Autocomplete (`Trie`)
```bash
curl "http://localhost:8080/api/commands/autocomplete?q=mk"
# Response: {"prefix":"mk","algorithmUsed":"Trie Prefix Traversal (O(K))","suggestions":["mkdir","mktemp"]}
```

### 2. Spelling Correction (`Levenshtein Edit Distance`)
```bash
curl "http://localhost:8080/api/commands/correct?q=grpe"
# Response: {"originalInput":"grpe","corrected":true,"suggestions":[{"command":"grep","editDistance":1,...}]}
```

### 3. Pattern Search (`Rabin-Karp`)
```bash
curl "http://localhost:8080/api/commands/search?q=file"
# Response: {"query":"file","algorithmUsed":"Rabin-Karp Rolling Hash","matchCount":63,...}
```

---

## 10. Security & Safety Guarantee

This application is strictly an educational command-information system.
The backend **NEVER** executes commands on the host operating system (no `Runtime.exec()`, `ProcessBuilder`, or shell execution). All queries perform read-only dataset retrieval and manual algorithm calculations.
