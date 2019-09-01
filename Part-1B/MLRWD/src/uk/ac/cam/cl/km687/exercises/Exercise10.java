package uk.ac.cam.cl.km687.exercises;

import uk.ac.cam.cl.mlrd.exercises.social_networks.IExercise10;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Exercise10 implements IExercise10 {

    /**
     * Load the graph file. Each line in the file corresponds to an edge; the
     * first column is the source node and the second column is the target. As
     * the graph is undirected, your program should add the source as a
     * neighbour of the target as well as the target a neighbour of the source.
     *
     * @param graphFile
     *            {@link Path} the path to the network specification
     * @return {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> For
     *         each node, all the nodes neighbouring that node
     */
    public Map<Integer, Set<Integer>> loadGraph(Path graphFile) throws IOException {
        Map<Integer, Set<Integer>> result = new HashMap<>();

        //Read through file and add edges to map
        BufferedReader br = Files.newBufferedReader(graphFile);
        String line;
        while ((line = br.readLine()) != null) {
            String[] nodes = line.split(" ");
            int nodeA = Integer.valueOf(nodes[0]);
            int nodeB = Integer.valueOf(nodes[1]);
            Set<Integer> setA = result.getOrDefault(nodeA, new HashSet<>());
            Set<Integer> setB = result.getOrDefault(nodeB, new HashSet<>());
            setA.add(nodeB);
            setB.add(nodeA);
            result.put(nodeA, setA);
            result.put(nodeB, setB);
        }
        return result;
    }


    /**
     * Find the number of neighbours for each point in the graph.
     *
     * @param graph
     *            {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> The
     *            loaded graph
     * @return {@link Map}<{@link Integer}, {@link Integer}> For each node, the
     *         number of neighbours it has
     */
    public Map<Integer, Integer> getConnectivities(Map<Integer, Set<Integer>> graph) {
        Map<Integer, Integer> result = new HashMap<>();
        for (Integer node : graph.keySet()) {
            Set<Integer> neighbours = graph.get(node);
            result.put(node, neighbours.size());
        }
        return result;
    }

    /**
     * Find the maximal shortest distance between any two nodes in the network
     * using a breadth-first search.
     *
     * @param graph
     *            {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> The
     *            loaded graph
     * @return <code>int</code> The diameter of the network
     */
    public int getDiameter(Map<Integer, Set<Integer>> graph) {
        int maxDistance = 0;
        for (Integer node : graph.keySet()) {
            int currentDistance = breadthFirstSearch(graph, node);
            if (currentDistance > maxDistance)
                maxDistance = currentDistance;
        }
        return maxDistance;
    }
    private int breadthFirstSearch(Map<Integer, Set<Integer>> graph, int source) {
        Map<Integer, Integer> seen = new HashMap<>();

        //Add first to node to toExplore
        List<Integer> toExplore = new LinkedList<>();
        toExplore.add(source);
        seen.put(source, 0);

        //traverse graph
        int current = -1;
        while (!toExplore.isEmpty()) {
            current = toExplore.remove(0);
            for (int neighbour : graph.get(current)) {
                if (!seen.containsKey(neighbour)) {
                    toExplore.add(neighbour);
                    seen.put(neighbour, seen.get(current) +1);
                }
            }
        }
        //curr should be the last that has been touched, and hence, the farthest
        return seen.get(current);
    }

}