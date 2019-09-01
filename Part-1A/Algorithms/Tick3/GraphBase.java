package uk.ac.cam.km687.Algorithms.Tick3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public abstract class GraphBase {

	protected int mN;  // Number of nodes
	protected int mM;  // Number of edges
	protected int mAdj[][]; // Graph as adjacency matrix

	/**
	 * Construct graph from a file available at a URL
	 * @param url URL for file
	 * @throws IOException
	 */
	public GraphBase(URL url) throws IOException {
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(url.openStream()));
		loadGraph(in);
	}

	/**
	 * Construct graph from file
	 * @param file Filename
	 */
	public GraphBase(String file) throws IOException {
		loadGraph(new BufferedReader(new FileReader(file)));
	}

	/**
	 * Construct graph from an adjacency matric
	 * @param adj
	 */
	public GraphBase(int adj[][]) {
		mAdj=adj.clone();
		for (int i=0; i<adj.length; i++) {
			mAdj[i]=adj[i].clone();
		}
		mN=mAdj.length;
		mM=0;
		for (int i=0; i<mN; i++) {
			for (int j=0; j<mN; j++) {
				if (mAdj[i][j]!=0) mM++;
			}
		}
	}

	/**
	 * Computes a path between two nodes that has the smallest number
	 * of edges using BFS. Ignores the edge weights.
	 * @param src  The source node ID
	 * @param target The sink node ID
	 * @return A list of node IDs for a path from src to target
	 * @throws TargetUnreachable Thrown if s and t are disconnected
	 */
	public abstract List<Integer> getFewestEdgesPath(int src, int target) throws TargetUnreachable;

	/**
	 * Compute the maximum flow between two nodes
	 * @param s Source node ID
	 * @param t Sink node ID
	 * @return Flow value and network
	 */
	public abstract MaxFlowNetwork getMaxFlow(int s, int t);

	// Getters
	public final int getNumVertices() { return mN; }
	public final int getWeight(int i, int j) throws InvalidEdgeException {
		if (i<mN && j<mN) {
			return mAdj[i][j];
		}
		else throw new InvalidEdgeException();
	}

	// Private function to load graph from a stream
	private final void loadGraph(BufferedReader bufferedReader) throws NumberFormatException, IOException {
		String line;
		if( (line = bufferedReader.readLine()) != null )
		{
			Scanner scanner = new Scanner(line);
			mM = Integer.parseInt(scanner.next());
			mN = Integer.parseInt(scanner.next());
			scanner.close();
		}

		mAdj = new int[mN][mN];
		for (int i=0; i < mN; i++) {
			for (int j=0; j < mN; j++) {
				mAdj[i][j] = 0;
			}
		}

		while((line = bufferedReader.readLine()) != null)
		{
			Scanner scanner = new Scanner(line);
			int i = Integer.parseInt(scanner.next());
			int j = Integer.parseInt(scanner.next());
			int w = Integer.parseInt(scanner.next());
			scanner.close();
			mAdj[i][j] = w;
		}
	}

}
