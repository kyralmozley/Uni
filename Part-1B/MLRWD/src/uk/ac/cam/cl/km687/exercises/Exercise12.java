package uk.ac.cam.cl.km687.exercises;

import uk.ac.cam.cl.mlrd.exercises.social_networks.IExercise12;

import java.nio.file.Path;
import java.util.*;

public class Exercise12 implements IExercise12 {

    /**
     * Compute graph clustering using the Girvan-Newman method. Stop algorithm when the
     * minimum number of components has been reached (your answer may be higher than
     * the minimum).
     *
     * @param graph
     *        {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> The
     *        loaded graph
     * @param minimumComponents {@link int} The minimum number of components to reach.
     * @return {@link List}<{@link Set}<{@link Integer}>>
     *        List of components for the graph.
     */
    public List<Set<Integer>> GirvanNewman(Map<Integer, Set<Integer>> graph, int minimumComponents) {
        Map<Integer, Set<Integer>> graphCopy = new HashMap<>(graph);

        List<Set<Integer>> subgraph = getComponents(graphCopy);
        while (subgraph.size() < minimumComponents) {
            Map<Integer, Map<Integer, Double>> between = getEdgeBetweenness(graphCopy);
            //remove edges with highest between
            List<Edge> toDelete = findMaxEdgesToRemove(between);
            for (Edge edge : toDelete) {
                graphCopy.get(edge.getNode()).remove(edge.getNeighbour());
            }

            subgraph = getComponents(graphCopy);
        }
        return subgraph;
    }

    public List<Edge> findMaxEdgesToRemove(Map<Integer, Map<Integer, Double>>between) {
        List<Edge> maxEdges = new ArrayList<>();
        double maxBetween = 0.0;
        for (int node : between.keySet()) {
            for (int neighbour : between.get(node).keySet()) {
                double currentBetween = between.get(node).get(neighbour);
                if (currentBetween > maxBetween) {
                    maxBetween = currentBetween;
                    maxEdges = new ArrayList<>();
                    maxEdges.add(new Edge(node, neighbour));
                }
                else if (currentBetween == maxBetween)
                    maxEdges.add(new Edge(node, neighbour));
            }
        }

        return maxEdges;
    }


    /**
     * Find the number of edges in the graph.
     *
     * @param graph
     *        {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> The
     *        loaded graph
     * @return {@link Integer}> Number of edges.
     */
    public int getNumberOfEdges(Map<Integer, Set<Integer>> graph) {
        int edges = 0;
        for (Set<Integer> neighbours : graph.values()) {
            edges += neighbours.size();
        }
        return edges/2; //divide by 2 due to handshaking lemma

    }

    /**
     * Find the number of components in the graph using a DFS.
     *
     * @param graph
     *        {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> The
     *        loaded graph
     * @return {@link List}<{@link Set}<{@link Integer}>>
     *        List of components for the graph.
     */
    public List<Set<Integer>> getComponents(Map<Integer, Set<Integer>> graph) {

        List<Set<Integer>> result = new LinkedList<>();

        //Create a set of nodes that havent been visited
        Set<Integer> notVisited = new HashSet<>(graph.keySet());

        Random r = new Random();
        //Apply dfs until all nodes have been seen
        while (notVisited.size() > 0) {
            List<Integer> keysAsArray = new ArrayList<>(notVisited);
            int nextSource = keysAsArray.get(r.nextInt(keysAsArray.size()));
            //Nodes when found shall be added to set, newCluster
            Set<Integer> newCluster = new HashSet<>();
            visit(graph, notVisited, nextSource, newCluster);
            result.add(newCluster);
        }
        return result;

    }
    private void visit(Map<Integer, Set<Integer>> graph, Set<Integer> notVisited, int curr, Set<Integer> result) {
        notVisited.remove(curr);
        result.add(curr);
        for (int neighbour : graph.get(curr)) {
            if (notVisited.contains(neighbour))
                visit(graph, notVisited, neighbour, result);
        }
    }

    /**
     * Calculate the edge betweenness.
     *
     * @param graph
     *         {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> The
     *         loaded graph
     * @return {@link Map}<{@link Integer},
     *         {@link Map}<{@link Integer},{@link Double}>> Edge betweenness for
     *         each pair of vertices in the graph
     */
    public Map<Integer, Map<Integer, Double>> getEdgeBetweenness(Map<Integer, Set<Integer>> graph) {
        //Set default values of between to 0.0
        Map<Integer, Map<Integer, Double>> between = new HashMap<>();

        for (Integer v : graph.keySet()) {
            Map<Integer, Double> inner = new HashMap<>();
            for (Integer w : graph.get(v)) {
                inner.put(w, 0.0);
            }
            between.put(v, inner);
        }


        for (Integer s : graph.keySet()) {
            Map<Integer, List<Integer>> predecessors = new HashMap<>();
            Map<Integer, Integer> distance = new HashMap<>();
            Map<Integer, Integer> cShortestPaths = new HashMap<>();
            Map<Integer, Double> dependency = new HashMap<>();

            List<Integer> queue = new LinkedList<>();
            List<Integer> stack = new LinkedList<>();
            //Initialise values
            for (Integer w : graph.keySet()) {
                predecessors.put(w, new LinkedList<>());
                distance.put(w, -1);
                cShortestPaths.put(w, 0);
            }
            distance.put(s, 0);
            cShortestPaths.put(s, 1);
            queue.add(s);

            //single-source shortest-path problem
            while (!queue.isEmpty()) {
                Integer v = queue.remove(0);
                stack.add(0, v);
                for (Integer w : graph.get(v)) {
                    //path discovery
                    if (distance.get(w) == -1) {
                        distance.put(w, distance.get(v) + 1);
                        queue.add(w);
                    }
                    //path counting
                    if (distance.get(w) == distance.get(v) + 1) {
                        cShortestPaths.put(w, cShortestPaths.get(w) + cShortestPaths.get(v));
                        predecessors.get(w).add(v);
                    }
                }
            }
            //accumulation
            for (Integer v : graph.keySet()) {
                dependency.put(v, 0.0);
            }
            while (!stack.isEmpty()) {
                Integer w = stack.remove(0);
                for (Integer v : predecessors.get(w)) {
                    double c = ((double) cShortestPaths.get(v) / (double) cShortestPaths.get(w)) * (1 + dependency.get(w));
                    between.get(v).put(w, between.get(v).get(w) + c);
                    dependency.put(v, dependency.get(v) + c);
                }
            }
        }
        return between;
    }

}
