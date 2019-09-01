package uk.ac.cam.cl.km687.exercises;

public class Edge {
    Integer mNode;
    Integer mNeighbour;

    public Edge(Integer node, Integer neighbour) {
        this.mNode = node;
        this.mNeighbour = neighbour;
    }

    public Integer getNode() {
        return mNode;
    }

    public Integer getNeighbour() {
        return mNeighbour;
    }
}
