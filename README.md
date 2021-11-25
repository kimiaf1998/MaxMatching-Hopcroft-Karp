# Maximum Matching using Hopcroft-Karp Algorithm


Java graphical implementation of Max Matching using Hopcroft-Karp algorithm

## Definition


A maximal matching is a matching M of a graph G where every edge in G has a non-empty intersection with at least one edge in M.

<img src="https://user-images.githubusercontent.com/47594854/143388256-8bb212bc-f46f-4b64-af8a-9b9f85e49699.png" width="100">


A maximum matching is a matching M of graph G that no two edges have the same vertex (matching part) and contains the maximum possible number of edges (maximum part).

<img src="https://user-images.githubusercontent.com/47594854/143388285-915f7b70-7034-4677-9b0b-83737a187bbb.png" width="100">

## Implementation



1) Initialize Maximal Matching M as empty
2) While there exists an Augmenting Path p
     Remove matching edges of p from M and add not-matching edges of p to M
3) Display matching M on the primary stage
