package ex1.src;

import java.io.Serializable;
import java.util.*;

public class WGraph_DS implements weighted_graph, Serializable {

    /**
     * This class represents an undirected & weighted graph data structure implementation in java.
     *
     * @param Vertices - represents the set of vertices we are working on in our graph,where every vertex contains a special key.
     * @param Edges - represents the association between each vertex to it's neighbour vertices.
     * @param V - is used to keep track of the the total number of vertices that appear in this graph.
     * @param E - is used to keep track of the total number of edges in the graph.
     * @param MC - is used to keep track after the number of changes that were made in the graph since it's initialized.
     */
    private HashMap<Integer, node_info> Vertices = new HashMap<>();
    private HashMap<Integer, Edge> Edges = new HashMap<>();

    private int V;
    private int E;
    private int MC;

    /**
     * Default constructor.
     */
    public WGraph_DS() {
        V = 0;
        E = 0;
        MC = 0;
    }


    /**
     * Deep copy constructor.
     */
    public WGraph_DS(weighted_graph other) {
        V = other.nodeSize();
        E = other.edgeSize();
        MC = other.getMC();
        for (node_info v : other.getV()) {
            Vertices.put(v.getKey(), new NodeInfo(v.getKey()));
            Edges.put(v.getKey(), new Edge());
        }
        for (Integer v : Vertices.keySet()) {
            for (Integer o : Edges.keySet()) {
                double w = getEdge(v, o);
                if (w >= 0) {
                    connect(v, o, w);
                }
            }
        }

    }

    /**
     * Given a key this method returns a node that this key is assigned to it.
     */
    @Override
    public node_info getNode(int key) {
        return Vertices.get(key);
    }

    /**
     * Returns true if and only if there is an edge between two nodes.
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        node_info v = getNode(node1);
        node_info u = getNode(node2);
        if (node1 == node2) return false;
        if (!Vertices.containsKey(node1) || !Vertices.containsKey(node2)) return false;
        return Edges.get(node1).e.containsKey(u);
    }
    @Override
    public boolean equals(Object o){
        if(this==o)
            return true;
        if (o instanceof weighted_graph){
            weighted_graph temp = (weighted_graph) o;
            if (this.nodeSize()!=temp.nodeSize() || this.edgeSize()!= temp.edgeSize())
                return false;
            for (node_info i : this.getV()){
                if(temp.getNode(i.getKey())== null)
                    return false;
                if (this.getV(i.getKey()).size() != temp.getV(i.getKey()).size())
                    return false;
            }
            return true;
        }
        return false;
    }


    /**
     * Given node1-node2 this method returns the weight of their edge.
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            return Edges.get(node1).getWeight(getNode(node2));
        }

        return -1;
    }

    /**
     * Given key this method adds a new node to the graph.
     */
    @Override
    public void addNode(int key) {
        node_info v = new NodeInfo(key);
        if (!Vertices.containsKey(key)) {
            Vertices.put(key, v);
            Edge n = new Edge();
            Edges.put(key, n);
            V++;
            MC++;
        } else {
            System.err.println("Already exists");
        }
    }

    /**
     * given node1 & node2 this method connects node1 - node2 and sets the edge between them to weight w
     * which is g.o.e.t 0
     */
    @Override
    public void connect(int node1, int node2, double w) {
        node_info v = getNode(node1);
        node_info u = getNode(node2);
        boolean primary=node1!=node2;
        boolean firstCondition = Vertices.containsKey(node1) && Vertices.containsKey(node2);
        boolean secondCondition = Edges.get(node1).hasNeighbor(u) && Edges.get(node2).hasNeighbor(v);
        if (primary && w >= 0 && firstCondition) {
            if (!secondCondition) {
                Edges.get(node1).e.put(u, w);
                Edges.get(node2).e.put(v, w);
                E++;
                MC++;
            }
            if (Edges.get(node1).getWeight(u) != w) {
                Edges.get(node1).setWeight(u, w);
                MC++;
            }
        }

    }

    /**
     * This method returns the whole collection of nodes this graph contains.
     */
    @Override
    public Collection<node_info> getV() {
        return Vertices.values();
    }

    /**
     * Given a special key node_id this method returns all the neighbor node
     * attached to node_id.
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        return Edges.get(node_id).getNeighbors();
    }

    /**
     * Give a node with key this method removes this node from them graph
     * in addition it removes all the neighbor nodes attached to it.
     * Therefore the complexity of this method will be : O(n).
     */
    @Override
    public node_info removeNode(int key) {
        node_info removed = getNode(key);
        if (Vertices.containsKey(key)) {
            for (node_info v : getV(key)) {
                removeEdge(key, v.getKey());
            }
            Edges.remove(key);
            Vertices.remove(key);
            return removed;
        } else return null;
    }

    /**
     * Given node1,node2 this method simply removes the edge between them.
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            Edges.get(node1).remove(getNode(node2));
            Edges.get(node2).remove(getNode(node1));
            E--;
            MC++;
        }
    }

    /**
     * This method displays the total number of nodes this graph possesses.
     */
    @Override
    public int nodeSize() {
        return Vertices.size();
    }

    /**
     * This  method displays the total number of edges this graph possesses.
     */
    @Override
    public int edgeSize() {
        return E;
    }

    /**
     * This method keeps track of the amount of changes that occurred in the graph
     * since we initialized it.
     */
    @Override
    public int getMC() {
        return MC;
    }



    public String toString() {
        StringBuilder answer = new StringBuilder("|V|=" + nodeSize() + ",|E|=" + edgeSize() + ", ModCount=" + getMC() + "\n");
        for (Integer i : Vertices.keySet()) {
            answer.append("key:").append(i);
            for (node_info v : getV(i)) {
                answer.append("-->").append(v).append("[w=").append(Edges.get(i).getWeight(v)).append("]), ");
            }
            answer.append("\n");
        }
        return answer.toString();
    }


    private  class NodeInfo implements node_info, Comparable<NodeInfo>, Serializable {
        /**
         * This class represents the set of vertices that Weighted & Undirected Graph DS will be using.
         *
         * @param key - is used to assign a key for each node
         * @param i  - makes our key unique
         * @param tag - will be useful for us to implement our class of algorithms.
         * @param info - will be useful for us to implement our class of algorithms.
         */
        private  int i = 0;
        private int key;
        private double tag;
        private String info;


        /**
         * default constructor
         */
        public NodeInfo() {
            key = i++;
            tag = 0;
            info = String.valueOf(key);
        }

        /**
         * copy constructor
         */
        public NodeInfo(node_info other) {
            this.key = other.getKey();
            this.tag = other.getTag();
            this.info = other.getInfo();
        }


        /**
         * additional constructor
         */
        public NodeInfo(int id) {
            this.key = id;
        }

        /**
         * @return - the unique key of each node.
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         * @return -returns the information of each node.
         */
        @Override
        public String getInfo() {
            return info;
        }

        /**
         * @param s-Defines a new information for specified node.
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }


        /**
         * @return - returns the tag that's assigned to specific node.
         */
        @Override
        public double getTag() {
            return tag;
        }

        /**
         * Defines a new tag for specified node.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        /**
         * This method is set for comparison/sorting purposes for our Object class NodeInfo
         */
        @Override
        public int compareTo(NodeInfo o) {
            return o.getTag() < tag ? 1 : -1;
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || this.getClass() != obj.getClass()) {
                return false;
            }
            NodeInfo v = (NodeInfo) obj;
            return key == v.key && Objects.equals(info, v.info);
        }

        @Override
        public String toString() {
            return key + "";
        }
    }

    private class Edge implements Serializable {

        /**
         * This class represents the set of neighbor nodes with assigned weight for each edge.
         *
         * @param neighbors - represents the neighbors of a specified node.
         */

        private HashMap<node_info, Double> e = new HashMap<>();
        private int ec;

        /**
         * default constructor
         */
        public Edge() {

            ec = 0;
        }

        /**
         * Check if the node is adjacent to other node.
         */
        boolean hasNeighbor(node_info key) {
            return e.containsKey(key);
        }

        /**
         * This methods sets the weight to:w for node v.
         */

        void setWeight(node_info v, double w) {
            e.put(v, w);
        }

        /**
         * returns the weight for node v.
         */
        double getWeight(node_info v) {
            return e.get(v);
        }


        void link(node_info v, double w) {
            setWeight(v, w);
            ec++;
        }

        /**
         * This method returns a collection of neighbors for some node.
         */
        Collection<node_info> getNeighbors() {
            Collection<node_info> neighbors = new ArrayList<>();
            neighbors.addAll(e.keySet());
            return neighbors;
        }

        /**
         * removes the node and the weight attached to it.
         */
        void remove(node_info v) {
            e.remove(v);

        }


        public String toString() {
            StringBuilder ans = new StringBuilder();
            for (Map.Entry<node_info, Double> entry : e.entrySet()) {
                ans.append("(").append(entry.getKey()).append(": ").append("w=").append(entry.getValue()).append("),").append("\n");
            }
            return ans.toString();
        }

    }


}
