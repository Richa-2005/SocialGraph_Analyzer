# SocialGraph_Analyzer

A Graph Theory-Based Social Network Analysis Tool

---

## Overview

SocialGraph Analyzer is a Java-based project that models and analyzes social networks using concepts from Discrete Mathematics (Relations & Graph Theory).

The system reads structured data (CSV/JSON) representing relationships between individuals and applies advanced algorithms to uncover hidden structural properties of the network.

Unlike basic graph traversal tools, this project focuses on:
- Mathematical properties of relations
- Transitive connectivity (degrees of separation)
- Group formation through equivalence relations

---

## Key Features

1. Graph Construction
- Builds graph from CSV/JSON input
- Uses Adjacency Matrix Representation
- Supports directed and undirected relationships


2. Relation Analysis 
Checks whether a relation is:
- Reflexive
- Symmetric
- Transitive
- Identifies equivalence relations
- Extracts Equivalence Classes (Friendship Circles)


3. Graph Algorithms 
- Implements Warshall’s Algorithm
- Computes Transitive Closure
- Determines:
- Degrees of separation between users
- Reachability matrix


4. Community Detection
- Identifies clusters of closely connected individuals
- Groups users into “social circles” based on relation properties


5. Visualization (Optional Enhancement)
- Graphical display of nodes and edges
- Visual representation of clusters and connections
- Implemented using Java Graphics2D / JFreeChart

---

## Core Concepts Used

### Discrete Mathematics
- Relations & Properties
- Equivalence Relations
- Graph Theory
- Adjacency Matrix
- Transitive Closure

### Algorithms
- Warshall’s Algorithm
- Matrix-based graph traversal

---

## System Architecture
```
Input (CSV/JSON)
        ↓
Data Parser
        ↓
Adjacency Matrix Builder
        ↓
Graph Engine
   ├── Relation Checker
   ├── Warshall Algorithm
   └── Equivalence Class Generator
        ↓
Output / Visualization

```

### Input Format

Example CSV:

```
PersonA,PersonB
Alice,Bob
Bob,Charlie
Charlie,Alice
David,Emma
```

Example JSON:
```
[
  {"from": "Alice", "to": "Bob"},
  {"from": "Bob", "to": "Charlie"},
  {"from": "Charlie", "to": "Alice"}
]
```


### Sample Output
```
	•	Relation Type: Symmetric, Not Transitive
	•	Friendship Circles:
	•	{Alice, Bob, Charlie}
	•	{David, Emma}
	•	Degrees of Separation:
	•	Alice → Charlie = 2
```


## Team Contributions

1. Richa Gupta – Graph Engine
- Adjacency matrix implementation
- Warshall’s Algorithm
- Transitive closure computation


2. Prushti Patel – Visualization
- Graph rendering using Java Graphics2D / JFreeChart
- Node-edge mapping
- Cluster highlighting


4. Tanvi Patel – Data & Analysis
- Dataset creation
- Relation verification (reflexive, symmetric, transitive)
- Documentation and result interpretation


## Tech Stack

- Java (Core + OOP)
- File Handling (CSV/JSON parsing)
- Java Collections Framework
- Graphics2D / JFreeChart 

---

## Future Enhancements
- Weighted graphs (strength of relationships)
- Real-time data input (API integration)
- Interactive UI (Swing/JavaFX)
- Advanced algorithms (Shortest Path, Centrality Measures)



## Use Cases
- Social network analysis
- Organizational relationship mapping
- Friend recommendation systems
- Community detection



## Why This Project Stands Out
- Combines theory (DMGT) with practical implementation
- Goes beyond standard BFS/DFS
- Demonstrates algorithmic thinking + mathematical rigor
- Easily extensible to real-world systems
