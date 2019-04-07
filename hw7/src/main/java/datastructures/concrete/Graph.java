package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoPathExistsException;
import datastructures.interfaces.IEdge;
// import misc.exceptions.NotYetImplementedException;
import misc.Sorter;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends IEdge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated than usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've constrained Graph
    //   so that E *must* always be an instance of IEdge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the IEdge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    private IDictionary<V, ISet<E>> listGraph; // use Adjacency List to represent our graph
    private IList<V> vertices;
    private IList<E> edges;

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        if (vertices == null || edges == null || vertices.contains(null) || edges.contains(null)) {
            throw new IllegalArgumentException();
        }
        listGraph = new ChainedHashDictionary<>();
        for (V vertex : vertices) {
            listGraph.put(vertex, new ChainedHashSet<>());
        }
        for (E edge : edges) {
            V v1 = edge.getVertex1();
            V v2 = edge.getVertex2();
            double weight = edge.getWeight();
            if (weight < 0 || !vertices.contains(v1) || !vertices.contains(v2)) {
                throw new IllegalArgumentException();
            }
            listGraph.get(v1).add(edge);
            listGraph.get(v2).add(edge);
        }
        this.edges = edges;
        this.vertices = vertices;
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        if (set == null) {
            throw new IllegalArgumentException();
        }
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return vertices.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return edges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        ISet<E> result = new ChainedHashSet<>();
        IDisjointSet<V> disjointVertices = new ArrayDisjointSet<>();
        for (V vertex : vertices) {
            disjointVertices.makeSet(vertex);
        }
        IList<E> sortedEdges = Sorter.topKSort(edges.size(), edges);
        for (E edge : sortedEdges) {
            V v1 = edge.getVertex1();
            V v2 = edge.getVertex2();
            if (disjointVertices.findSet(v1) != disjointVertices.findSet(v2)) {
                result.add(edge);
                disjointVertices.union(v1, v2);
            }
        }
        return result;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     * @throws IllegalArgumentException if start or end is null
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        // if start or end is null
        if (start == null || end == null) {
            throw new IllegalArgumentException();
        }
        IList<E> result = new DoubleLinkedList<>();
        // return an empty list if the start and end vertices are the same
        if (start.equals(end)) {
            return result;
        }

        // Initialize MPQ and add vertex
        IPriorityQueue<Node> nodeMPQ = new ArrayHeap<>();
        // keep track of visited node
        IDictionary<V, Node> visited = new ChainedHashDictionary();
        // store all the vertex and nodes
        IDictionary<V, Node> allNodes = new ChainedHashDictionary();
        for (V vertex : this.vertices) {
            Node vNode;
            // cost from start to start is 0
            if (vertex.equals(start)) {
                vNode = new Node(vertex, 0, null);
            } else {
                vNode = new Node(vertex); // send original cost to infinity
            }
            nodeMPQ.insert(vNode);
            allNodes.put(vertex, vNode);
        }

        while (visited.size() < allNodes.size() && !visited.containsKey(end)) {
            Node currentNode = nodeMPQ.removeMin();
            V currentVertex = currentNode.getVertex();

            if (!visited.containsKey(currentVertex)) {
                visited.put(currentVertex, currentNode); // mark current vertex / node as processed
                if (!currentVertex.equals(end)) {
                    for (E edge : listGraph.get(currentVertex)) {
                        V otherVertex = edge.getOtherVertex(currentVertex);
                        double newCost = currentNode.cost + edge.getWeight();
                        if (newCost < allNodes.get(otherVertex).cost) {
                            Node betterNode = new Node(otherVertex, newCost, edge);
                            allNodes.put(otherVertex, betterNode);
                            nodeMPQ.insert(betterNode);
                        }
                    }
                }
            }
        }
        if (!visited.containsKey(end) || visited.get(end).cost >= Double.POSITIVE_INFINITY) {
            throw new NoPathExistsException();
        }

        Node endNode = visited.get(end);
        // only starting node's prev is null
        while (endNode.predecessor != null) {
            E edge = endNode.predecessor;
            result.insert(0, edge);
            endNode = visited.get(edge.getOtherVertex(endNode.vertex));
        }
        return result;
    }

    private class Node implements Comparable<Node> {
        private V vertex;
        private double cost;
        private E predecessor;

        public Node(V vertex) {
            this(vertex, Double.POSITIVE_INFINITY, null);
        }

        public Node(V vertex, double cost, E predecessor) {
            this.vertex = vertex;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public V getVertex() {
            return vertex;
        }

        public int compareTo(Graph<V, E>.Node other) {
            if (this.cost > other.cost) {
                return 1;
            } else if (this.cost < other.cost) {
                return -1;
            }
            return 0;
        }
    }
}
