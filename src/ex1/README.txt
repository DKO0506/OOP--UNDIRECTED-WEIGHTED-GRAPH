# Ex1




### Author:
David Kokiashvili 
-----------


## Summary:
-------
The project focuses  on weighted undirected graph data structure and has two main Classes and two subclasses.
1. ex1.src.node_info - an interface that represents a node in the graph implemented by NodeInfo subclass.
2. Edges - class that represents the set of weighted edges of each node in the graph.
3. ex1.src.weighted_graph - an interface that represents an undirected weighted graph data structure implemented by ex1.src.WGraph_DS.
4. ex1.src.weighted_graph_algorithms - an interface that represents a set of algorithms to perform on a graph implemented by ex1.src.WGraph_Algo.
 
## Setup and data structure
My implementation for the WGraph data structure is using Hashmap of vertices & edges.
## NodeInfo class methods:
------ 
all the methods below are O(1)
1. getKey – returns the key (id) associated to a specific node.
2. getInfo  – returns the info of the node.
3. setInfo – sets the info of the node
4. getTag – returns the tag of the node
5. setTag - sets the tag of the node

## Edges class methods:
-----
all methods below are O(1).
1. setWeight - sets a specific weight to the destination node.
2. getWeight - returns the weight of a specified node.
3. link - assigns a weight to a node.
4. hasNeighbor - return true if a node has neighbor node with the specified key.
5. getNeighbors - return all the neighbor nodes of a given node
6. remove - removes the weight of an edge from the map.

## ex1.src.WGraph_DS class methods:
-----
1. getNode – returns a node the graph contains with a specific key. O(1)
2. hasEdge – checks if there is  an edge between 2 nodes. O(1)
3. getEdge - returns the weight associated to to the between node1-node2. O(1)
3. addNode – Adds a node to the graph O(1)
4. connect – connects an edge between 2 nodes and assigns the weight to their edge. O(1)
5. getV() - returns a collection of all the nodes in the graph O(1)
6. getV(node_id) - returns a collection of all the adjacent nodes of a node with the node_id O(1)
7. removeNode -  removes a node from the graph and all its edges O(n)
8. removeEdge – Removes an edge between 2 given nodes from the graph O(1)
9. nodeSize – Returns the total number of nodes in the graph O(1)
10. edgeSize – returns the total number of edges in the graph O(1)
11. getMC – returns the total number of changes made in the graph O(1)

## Graph_Algo class methods: 
-----
1. init - Initializes a graph that the class of the algorithms will perform on. O(1)
2. copy - makes a deep copy of the graph O(V+E)
3. isConnected - checks if there is a path from every 2 nodes in the graph , traversing the graph using BFS algorithm on weighted graphs starting from an arbitary node marking every node and checking in the end if all the nodes in the graph were marked O(V+E)
4. shortestPathDist() - returns the number of edges in the shortest path between 2 given nodes (src,dest) , returns -1 if there is no path , traversing the graph starting from src using BFS and marking every reachable node tag in the graph with his distance from src. O(V+E)
5. shortestPath() - return the shortest path between src node and dest node using Dijkstra's algorithm,traversing the neighbor nodes of the dequeued node from  and updating it's neighbor distance to the minimum between their edge and the removed + their edge weight, update the distance of the neighbour node and insert the removed to the map of predecessors.
   Insert the destination node to the list and as long the distance is positive insert all the previous nodes to list, reverse the list and return it. O(E*log(V)). 
