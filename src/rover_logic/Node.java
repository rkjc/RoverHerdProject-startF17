package rover_logic;

import common.Coord;

/**
 * Created by samskim on 5/12/16.
 */
// Node class for Astar search
public class Node implements Comparable<Node> {
    private Coord coord;
    private double data;
    //   private Node parent;

    public Node(Coord coord, double data) {
        this.coord = coord;
        this.data = data;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    @Override
    public int compareTo(Node other) {
        return (int) Math.ceil(this.data - other.data) * 10;
    }

    // only check by its coordinate, not data
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node))
            return false;
        if (this == o)
            return true;
        Node other = (Node) o;
        return this.getCoord().equals(other.getCoord());
    }

    @Override
    public int hashCode() {
        return this.getCoord().hashCode();
    }

    public String toString() {
        String str = "";
        str += "coord: " + coord + ", data: " + data;
        return str;
    }

}