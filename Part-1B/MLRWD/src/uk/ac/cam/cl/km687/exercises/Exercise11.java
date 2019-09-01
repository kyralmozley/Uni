package uk.ac.cam.cl.km687.exercises;

import uk.ac.cam.cl.mlrd.exercises.social_networks.IExercise11;
import uk.ac.cam.cl.km687.exercises.Exercise10;

import java.nio.file.Path;
import java.util.Map;
import java.io.IOException;
import java.util.*;

public class Exercise11 implements IExercise11 {

    /**
     * Load the graph file. Use Brandes' algorithm to calculate the betweenness
     * centrality for each node in the graph.
     *
     * @param graphFile
     *            {@link Path} the path to the network specification
     * @return {@link Map}<{@link Integer}, {@link Double}> For
     *         each node, its betweenness centrality
     */
    Exercise10 exercise10 = new Exercise10();
    
    public Map<Integer, Double> getNodeBetweenness(Path graphFile) throws IOException {
        Map<Integer, Set<Integer>> graph = exercise10.loadGraph(graphFile);

        Map<Integer, Double> between = new HashMap<>();
        for (Integer v : graph.keySet())
            between.put(v, 0.0);

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
            while(!queue.isEmpty()) {
                Integer v = queue.remove(0);
                stack.add(0, v);
                for (Integer w : graph.get(v)) {
                    //path discovery
                    if (distance.get(w) == -1) {
                        distance.put(w, distance.get(v) +1);
                        queue.add(w);
                    }
                    //path counting
                    if (distance.get(w) == distance.get(v) +1) {
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
                    dependency.put(v, dependency.get(v) +
                            ((double) cShortestPaths.get(v)/(double) cShortestPaths.get(w))*(1+ dependency.get(w)));
                }
                if (w != s) {
                    between.put(w, between.get(w) + dependency.get(w));
                }
            }
        }
        for (Integer v : between.keySet()) {
            between.put(v, between.get(v)/2);
        }
        return between;
    }
}