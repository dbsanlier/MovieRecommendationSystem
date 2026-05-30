# Movie Recommendation System

A Java desktop application that recommends movies using **user-based collaborative filtering**. The engine computes cosine similarity between user rating vectors and ranks candidates with a custom pointer-based Max Heap, then presents results through a Java Swing GUI.

---

## Features

- **Two recommendation modes:**
  - **Mode A** — Select a pre-loaded target user and get recommendations instantly
  - **Mode B** — Rate movies manually (1–5) and receive personalised suggestions
- Custom **node-based Max Heap** for O(log n) neighbour ranking
- **Cosine similarity** computed across 9,018 movie dimensions
- Configurable **X** (number of similar users) and **K** (films per user) parameters
- Status bar showing loaded dataset counts on startup

---

## Project Structure

```
MovieRecommendationSystem/
├── src/main/java/
│   ├── Main.java                  # Entry point — loads data, opens GUI
│   ├── DataLoader.java            # CSV parsing and in-memory storage
│   ├── CosineSimilarity.java      # Static similarity computation
│   ├── HeapNode.java              # Max Heap node (userId, similarity, pointers)
│   ├── MaxHeap.java               # Pointer-based binary max-heap
│   ├── RecommendationEngine.java  # Orchestrates the full pipeline
│   ├── MainScreen.java            # GUI — entry panel
│   ├── TargetUserScreen.java      # GUI — known target user flow
│   └── MovieRatingFrame.java      # GUI — manual rating flow
├── main_data.csv                  # 600 users × 9,018 movie rating columns
├── target_user.csv                # 10 pre-defined target users
├── movies.csv                     # 5,401 movieId → title mappings
└── pom.xml
```

---

## Requirements

- Java 8 or higher
- Maven 3.x

---

## Getting Started

**1. Clone the repository**
```bash
git clone https://github.com/your-username/MovieRecommendationSystem.git
cd MovieRecommendationSystem
```

**2. Build with Maven**
```bash
mvn clean package
```

**3. Run**
```bash
mvn exec:java -Dexec.mainClass="Main"
```

Or run the JAR directly:
```bash
java -jar target/MovieRecommendationSystem.jar
```

> **Note:** The CSV files (`main_data.csv`, `target_user.csv`, `movies.csv`) must be in the working directory when the application is launched.

---

## How It Works

### Algorithm

1. The target user's ratings are loaded as a vector of 9,018 dimensions (one per movie).
2. Cosine similarity is computed between the target vector and all 600 users in the database.
3. Each user + score pair is inserted into the **Max Heap**. The root always holds the most similar user.
4. `extractMax()` is called repeatedly to retrieve the top neighbours in order.
5. From each neighbour, the K highest-rated films with a known title are collected.
6. Results are displayed grouped by similar user.

### Max Heap — Binary Index Navigation

The heap uses pointer-based nodes (no backing array). Any node at position `p` is reached in O(log n) by converting `p` to binary and following the bits after the leading `1`:

```
Position 6  →  binary "110"  →  skip leading 1  →  bits: 1, 0
  bit 1  →  go RIGHT  (root → pos 3)
  bit 0  →  go LEFT   (pos 3 → pos 6)
Reached in floor(log₂ 6) = 2 steps
```

### Cosine Similarity

```
cos(A, B) = (A · B) / (‖A‖ × ‖B‖)
```

Unrated movies (value = 0) contribute nothing to the dot product or norms. Returns 0.0 if either user has no ratings.

---

## Usage

### Mode A — Target User

1. Launch the app and click **"A — Recommendation Based on Target User"**
2. Select a user from the dropdown (IDs 601–610)
3. Set **X** (how many similar users to use) and **K** (how many films per user)
4. Click **Get Recommendation**

Output: `X × K` film recommendations grouped by similar user.

### Mode B — Manual Rating

1. Launch the app and click **"B — Recommendation Based on Movie Rating"**
2. Rate the displayed films using the 1–5 scale (leave unrated films at 0)
3. Set **X** and **K**, then click **Get Recommendation**

Output: Same format as Mode A, based on your own ratings.

---

## Dataset

| File | Description | Size |
|------|-------------|------|
| `main_data.csv` | User–movie rating matrix | 600 users, 9,018 columns |
| `target_user.csv` | Pre-defined target users | 10 users |
| `movies.csv` | Movie ID to title mapping | 5,401 titles |

Ratings range from 1 to 5; a value of 0 means the film has not been rated.

---

## Time Complexity

| Operation | Complexity |
|-----------|------------|
| Load CSVs | O(n × m) |
| Compute all similarities | O(n × m) |
| Insert all users into heap | O(n log n) |
| Extract X neighbours | O(X log n) |
| findNode navigation | O(log n) |

`n` = 600 users, `m` = 9,018 movie dimensions
