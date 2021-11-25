# Maximum Matching using Hopcroft-Karp Algorithm


Java graphical implementation of Max Matching using Hopcroft-Karp algorithm

## Definition


A maximal matching is a matching M of a graph G where every edge in G has a non-empty intersection with at least one edge in M.
A maximum matching is a matching M of graph G that no two edges have the same vertex (matching part) and contains the maximum possible number of edges (maximum part).

## Implementation



1) Initialize Maximal Matching M as empty
2) While there exists an Augmenting Path p
     Remove matching edges of p from M and add not-matching edges of p to M
3) Display matching M on the primary stage
