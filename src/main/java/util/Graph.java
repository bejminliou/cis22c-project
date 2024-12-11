package util;

import java.util.ArrayList;

/**
 * Graph.java
 *
 * @author Benjamin Liou
 * @author Kevin Young
 * @author Rolen Louie
 * @author Yukai Qiu
 * @author Kenneth Garcia
 * @author Tu Luong
 * CIS 22C, Lab 16
 */
public class Graph {
    private int vertices;
    private int edges;
    private ArrayList<LinkedList<Integer>> adj;
    private ArrayList<Character> color;
    private ArrayList<Integer> distance;
    private ArrayList<Integer> parent;
    private ArrayList<Integer> discoverTime;
    private ArrayList<Integer> finishTime;
    private static int time = 0;

    /** Constructors and Destructors */

    /**
     * initializes an empty graph, with n vertices and 0 edges
     *
     * @param numVtx the number of vertices in the graph
     * @throws IllegalArgumentException when numVtx <= 0
     * @precondition numVtx > 0
     */
    public Graph(int numVtx) throws IllegalArgumentException {
        if (numVtx <= 0) {
            throw new IllegalArgumentException("Number of vertex must be positive");
        }

        vertices = numVtx;
        edges = 0;

        adj = new ArrayList<>(numVtx);
        color = new ArrayList<>(numVtx);
        distance = new ArrayList<>(numVtx);
        parent = new ArrayList<>(numVtx);
        discoverTime = new ArrayList<>(numVtx);
        finishTime = new ArrayList<>(numVtx);

        for (int i = 0; i < numVtx; i++) {
            adj.add(new LinkedList<>());
            color.add('W');
            distance.add(-1);
            parent.add(null);
            discoverTime.add(null);
            finishTime.add(null);
        }
    }

    /*** Accessors ***/

    /**
     * Returns the number of edges in the graph
     *
     * @return the number of edges
     */
    public int getNumEdges() {
        return edges;
    }

    /**
     * Returns the number of vertices in the graph
     *
     * @return the number of vertices
     */
    public int getNumVertices() {
        return vertices;
    }

    /**
     * returns whether the graph is empty (no edges)
     *
     * @return whether the graph is empty
     */
    public boolean isEmpty() {
        return edges == 0;
    }

    /**
     * Returns the value of the distance[v]
     *
     * @param v a vertex in the graph
     * @return the distance of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public Integer getDistance(Integer v) throws IndexOutOfBoundsException {
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex out of bounds");
        }
        return distance.get(v - 1);
    }

    /**
     * Returns the value of the parent[v]
     *
     * @param v a vertex in the graph
     * @return the parent of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public Integer getParent(Integer v) throws IndexOutOfBoundsException {
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex out of bounds");
        }
        return parent.get(v - 1) == null ? 0 : parent.get(v - 1) + 1;
    }

    /**
     * Returns the value of the color[v]
     *
     * @param v a vertex in the graph
     * @return the color of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public Character getColor(Integer v) throws IndexOutOfBoundsException {
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex out of bounds");
        }
        return color.get(v - 1);
    }

    /**
     * Returns the value of the discoverTime[v]
     *
     * @param v a vertex in the graph
     * @return the discover time of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public Integer getDiscoverTime(Integer v) throws IndexOutOfBoundsException {
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex out of bounds");
        }
        return discoverTime.get(v - 1) == null ? -1 : discoverTime.get(v - 1);
    }

    /**
     * Returns the value of the finishTime[v]
     *
     * @param v a vertex in the graph
     * @return the finish time of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public Integer getFinishTime(Integer v) throws IndexOutOfBoundsException {
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex out of bounds");
        }
        return finishTime.get(v - 1) == null ? -1 : finishTime.get(v - 1);
    }

    /**
     * Returns the LinkedList stored at index v
     *
     * @param v a vertex in the graph
     * @return the adjacency LinkedList at v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public LinkedList<Integer> getAdjacencyList(Integer v) throws IndexOutOfBoundsException {
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex out of bounds");
        }
        LinkedList<Integer> adjList = new LinkedList<>();

        adj.get(v - 1).positionIterator();
        while (!adj.get(v - 1).offEnd()) {
            adjList.addLast(adj.get(v - 1).getIterator() + 1);
            adj.get(v - 1).advanceIterator();
        }

        return adjList;
    }

    /*** Manipulation Procedures ***/

    /**
     * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into
     * the list at index u) @precondition, 0 < u, v <= vertices
     *
     * @param u a vertex in the graph
     * @param v a vertex in the graph
     * @throws IndexOutOfBounds exception when u or v is out of bounds
     */
    public void addDirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
        if (u <= 0 || u > vertices || v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex out of bounds");
        }
        adj.get(u - 1).addLast(v - 1);
        edges++;
    }

    /**
     * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into
     * the list at index u) and inserts u into the adjacent vertex list of v.
     *
     * @param u a vertex in the graph
     * @param v a vertex in the graph
     * @throws IndexOutOfBoundsException when u or v is out of bounds
     * @precondition, 0 < u, v <= vertices
     */
    public void addUndirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
        if (u <= 0 || u > vertices || v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex out of bounds");
        }
        adj.get(u - 1).addLast(v - 1);
        adj.get(v - 1).addLast(u - 1);
        edges++;
    }

    /*** Additional Operations ***/

    /**
     * Creates a String representation of the Graph Prints the adjacency list of
     * each vertex in the graph, vertex: <space separated list of adjacent
     * vertices>
     *
     * @return a space separated list of adjacent vertices
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vertices; i++) {
            sb.append(i + 1).append(": ");

            adj.get(i).positionIterator();

            while (!adj.get(i).offEnd()) {
                sb.append(adj.get(i).getIterator() + 1).append(" ");
                adj.get(i).advanceIterator();
            }

            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Performs breath first search on this Graph give a source vertex
     *
     * @param source the starting vertex
     * @throws IndexOutOfBoundsException when the source vertex is out of bounds
     *                                   of the graph
     * @precondition source is a vertex in the graph
     */
    public void BFS(Integer source) throws IndexOutOfBoundsException {
        if (source <= 0 || source > vertices) {
            throw new IndexOutOfBoundsException("Vertex out of bounds");
        }

        int s = source - 1;

        for (int i = 0; i < vertices; i++) {
            color.set(i, 'W');
            distance.set(i, -1);
            parent.set(i, null);
        }

        color.set(s, 'G');
        distance.set(s, 0);
        parent.set(s, null);

        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(s);

        while (!queue.isEmpty()) {
            int u = queue.getFirst();
            queue.removeFirst();

            LinkedList<Integer> adjlist = adj.get(u);
            adjlist.positionIterator();

            while (!adjlist.offEnd()) {
                int v = adjlist.getIterator();

                if (color.get(v) == 'W') {
                    color.set(v, 'G');
                    distance.set(v, distance.get(u) + 1);
                    parent.set(v, u);
                    queue.addLast(v);
                }
                adjlist.advanceIterator();
            }

            color.set(u, 'B');
        }


    }

    /**
     * Performs depth first search on this Graph in order of vertex lists
     */
    public void DFS() {
        for (int i = 0; i < vertices; i++) {
            color.set(i, 'W');
            parent.set(i, null);
            discoverTime.set(i, -1);
            finishTime.set(i, -1);
        }

        time = 0;

        for (int i = 0; i < vertices; i++) {
            if (color.get(i) == 'W') {
                visit(i);
            }
        }
    }

    /**
     * Private recursive helper method for DFS
     *
     * @param vertex the vertex to visit
     */
    private void visit(int vertex) {
        color.set(vertex, 'G');
        time++;
        discoverTime.set(vertex, time);

        LinkedList<Integer> adjlist = adj.get(vertex);
        adjlist.positionIterator();

        while (!adjlist.offEnd()) {
            int neighbor = adjlist.getIterator();
            if (color.get(neighbor) == 'W') {
                parent.set(neighbor, vertex);
                visit(neighbor);
            }
            adjlist.advanceIterator();
        }

        color.set(vertex, 'B');
        time++;
        finishTime.set(vertex, time);
    }
}
