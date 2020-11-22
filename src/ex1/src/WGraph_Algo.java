package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {
    /**
     * This class focuses on three main methods :
     *
     * 1) Checking the connectivity of weighted & undirected graph.
     *
     * 2) Finding the shortest path distance from a given node to some destination node you'd pick.
     *
     * 3) Finding the shortest path from one node to another.
     *
     * The main Data Structure used in this class will be the
     *
     * @param algo - which represents the graph that we want to implement our algorithms on.
     */


    private weighted_graph algo;


    /** Default constructor */
    public WGraph_Algo() {
        algo = new WGraph_DS();
    }


    /** Initialization of the our graph. */
    @Override
    public void init(weighted_graph g) {
        this.algo = g;
    }

    /**
     * @return - the graph that we are working on.
     */
    @Override
    public weighted_graph getGraph() {
        return algo;
    }


    /** This method provides a deep copy for this graph. */
    @Override
    public weighted_graph copy() {
        setGraph(0);
        weighted_graph copyG = new WGraph_DS(algo);

        return copyG;
    }

    /** Sets the sets the distances of every node in this graph.  */
    private void setGraph(double distances ) {
        for (node_info v : algo.getV()) {
            v.setTag(distances);
        }
    }

    /**
     * In this method we are asking: When given some kind of undirected & weighted graph
     *
     * is it possible to reach to every node in the graph while given some arbitrary node to start with.
     *
     *  Algorithm Description:
     *
     *  1) First things first: if the graph is empty or it contains only one node then.
     *
     *  2) Tag the nodes with unique labels to determine if they are visited or not. In this method tag
     *
     *  determines the following: If the tag of a node is 0 then this node is unvisited, if the tag is 1 than this node is visited.
     *
     *  3) Initialize a priority queue. Choose an arbitrary node , set it's tag to 1 to indicate if it's visited or not.
     *      Insert it to the queue.
     *
     *  4) As long as the queue isn't empty: dequeue the node from the queue
     *
     *  5) Traverse on the neighbor nodes of the dequeued node: If the tag is 0 then update it to 1 and insert it to the queue.
     *
     *  6) If in the end there will be a node with tag of 0 it means that the graph is not connected
     *  otherwise if the tag is 1 then the graph is connected.
     *
     * @return
     */
    @Override
    public boolean isConnected() {

        if (algo.nodeSize() == 0 || (algo.nodeSize() == 1 && algo.edgeSize() == 0)) return true;


        setGraph(0);
        Queue<node_info> pq = new PriorityQueue<>();
        for (node_info v : algo.getV()) {
            v.setTag(1);
            pq.add(v);
            break;
        }

        while (!pq.isEmpty()) {
            node_info current = pq.remove();
            for (node_info v : algo.getV(current.getKey())) {
                if (v.getTag() == 0) {
                    pq.add(v);
                    v.setTag(1);
                }
            }
        }
        for (node_info v : algo.getV()) {
            if (v.getTag() == 0) {
                return false;
            }
        }
        return true;


    }


    @Override
    public double shortestPathDist(int src, int dest) {

        if (src == dest) return 0;


        List<node_info> s = new LinkedList<>();
        s = shortestPath(src, dest);

        if (s == null) return -1;

        node_info source = algo.getNode(src);
        node_info destination = algo.getNode(dest);
        return destination.getTag();
    }


    /**
     *
     * This algorithm is based on Dijkstra's algorithm on weighted graph to find the shortest path from
     * a source node to other nodes in the graph.
     *
     * At start we set the distance of every node to positive infinity and the path to -1.
     *
     * Algorithm Description:
     *
     * 1) Set all the tags(distances) to positive infinity.
     * 2) Initialize a queue and insert the source node to the queue
     *    Initialize a map predecessors and insert the source node's index to it.
     * 3) If the graph is empty than nothing will be returned.
     *    * If the graph contains only one node or the source node is the target node
     *    than add it to the list and return it.
     *
     * 4) As long as the queue isn't empty: dequeue the node from the queue
     *    Traverse all of it's neighbours: if the weight of the removed node and it's neighbour is less than neighbour  node's weight
     *    update the neighbours weight to the minimum weight, insert that node to queue.
     *    Insert the removed node data to predecessors map.
     *
     * 5) Insert the target node to shortest path list and as long as it's distance is positive
     *    insert all the predecessor nodes to the list reverse it
     *    and return it.
     *
     *
     *
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        setGraph(Double.POSITIVE_INFINITY);


        node_info sourceNode = algo.getNode(src);
        node_info destination = algo.getNode(dest);
        if (sourceNode == null || destination == null) return null;
        sourceNode.setTag(0);

        Queue<node_info> pq = new PriorityQueue<>();
        HashMap<Integer, Integer> pred = new HashMap<>();
        List<node_info> shortest = new LinkedList<>();
        if (algo.nodeSize() == 1 || dest == src) {
            shortest.add(sourceNode);
            return shortest;
        }


        pred.put(src, -1);

        pq.add(sourceNode);

        while (!pq.isEmpty()) {
            node_info re = pq.remove();
            if (re.getKey() == dest) break;
            for (node_info v : algo.getV(re.getKey())) {
                double weight = algo.getEdge(re.getKey(), v.getKey());
                double compare = re.getTag() + weight;
                if (v.getTag() > compare) {
                    pred.put(v.getKey(), re.getKey());
                    pq.add(v);
                    v.setTag(compare);
                }
            }
        }
        while (dest >= 0) {
            shortest.add(algo.getNode(dest));
            dest = pred.get(dest);
        }
        Collections.reverse(shortest);
        System.out.println(shortest);

        return shortest;
    }

    @Override
    public boolean save(String file) {
        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(algo);
            out.close();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public boolean load(String file) {
        try {
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(f);
            weighted_graph loadedGraph = (weighted_graph) in.readObject();
            in.close();
            f.close();
            this.init(loadedGraph);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

