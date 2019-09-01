package uk.ac.cam.km687.Algorithms.Tick3;
import uk.ac.cam.rkh23.Algorithms.Tick3.GraphBase;
import uk.ac.cam.rkh23.Algorithms.Tick3.InvalidEdgeException;
import uk.ac.cam.rkh23.Algorithms.Tick3.TargetUnreachable;
import uk.ac.cam.rkh23.Algorithms.Tick3.MaxFlowNetwork;

import java.io.*;
import java.util.*;
import java.net.URL;

public class Graph extends GraphBase {

    int V = super.mN;
    int E = super.mM;
    int parent[] = new int[V];
    boolean visited[];
    int mAdj[][] = super.mAdj;

    //constructors
    public Graph(URL url) throws IOException {
      super(url);
    }

    public Graph(String file) throws IOException {
      super(file);
    }

    public Graph(int adj[][]) {
      super(adj);
    }


    public List<Integer> getFewestEdgesPath(int src, int target) throws TargetUnreachable {
      //WORKS FINE
      parent = new int[V];
      visited = new boolean[V];

      for(int i=0; i<V; i++)
        visited[i]=false;

      // create a queue, enqueue source vertex
      LinkedList<Integer> queue = new LinkedList<Integer>();
      queue.add(src);
      visited[src] = true;
      parent[src]=-1;

      // Standard BFS Loop
      while (queue.size()!=0) {
        int u = queue.poll();
        for (int v=0; v<V; v++) {
          if (visited[v]==false && this.mAdj[u][v] > 0){
            queue.add(v);
            parent[v] = u;
            visited[v] = true;
          }

        }
      }

      if(visited[target] == false) {
        throw new TargetUnreachable();
      }
      //get route
      List<Integer> route = new ArrayList<Integer>();
      route.add(0, target);

      while(route.get(0) != src) {
        int current = route.get(0);
        route.add(0, parent[current]);
      }


      return route;
    }

    public boolean bfs(int s, int t) {
      //return if path exists
        try {
          getFewestEdgesPath(s,t);
        } catch (TargetUnreachable e) {
          return false;
        }
        return (visited[t]==true);
    }


    public MaxFlowNetwork getMaxFlow(int s, int t) {
      //inial flow = 0
      int max_flow = 0;

      while(bfs(s, t)) {
        //while a path exists, augument flow
        int path_flow = Integer.MAX_VALUE;
        for (int v=t; v!=s; v=parent[v]) {
          int u = parent[v];
          path_flow = Math.min(path_flow, this.mAdj[u][v]);
        }
        // update residual capacities of the edges and reverse edges along the path
        for (int v=t; v != s; v=parent[v]) {
          int u = parent[v];
          this.mAdj[u][v] -= path_flow;
          this.mAdj[v][u] += path_flow;
        }

          // Add path flow to overall flow
          max_flow += path_flow;
      }

      GraphBase g = new Graph(this.mAdj);
      MaxFlowNetwork flownet = new MaxFlowNetwork(max_flow, g);
      //System.out.println(flownet.getFlow());
      return flownet;
    }

    public static boolean isValidURL(String url) {
      // Try creating a valid URL
      try {
        new URL(url).toURI();
        return true;
      }
      catch (Exception e) {
        return false; //
      }
    }

      public static void main (String[] args) throws java.lang.Exception {
        Graph m;

        String input = args[0];

        if(isValidURL(input)) {
          URL url_input = new URL(input);
          m = new Graph(url_input);

        } else if(new File(input).exists()) {
          m = new Graph(input);

        } else {
        //create a matrix from string input
          input = input.replace("[","");
          input = input.substring(0, input.length() - 2);
          String[] arr = input.split("]");
          for(int i=1; i<arr.length;i++) {
            arr[i] = arr[i].substring(1);
          }

          String[][] mat = new String[arr.length][arr.length];
          int[][] input_matrix = new int[arr.length][arr.length];


          for(int i=0; i<arr.length; i++) {
              mat[i] = arr[i].split(",");
            }

            for(int i=0; i<arr.length; i++) {
              for(int j=0; j<arr.length; j++) {
                input_matrix[i][j]= Integer.parseInt(mat[i][j]);
              }
            }
             m = new Graph(input_matrix);
          }

/*




*/
        int target = m.mAdj.length -1;
        //m.getFewestEdgesPath(0,target);
        m.getMaxFlow(0,target);


         }
     }
