package rover_logic;

import common.Coord;
import common.MapTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import enums.RoverDriveType;
import enums.Terrain;

/**
 * Created by samskim on 5/12/16.
 */
public class SearchLogic {
    // ******* Search Methods

    public List<String> Astar(Coord current, Coord dest, MapTile[][] scanMapTiles, RoverDriveType drive, Map<Coord, MapTile> globalMap) {
        PriorityQueue<Node> open = new PriorityQueue<>();
        Set<Node> closed = new HashSet<>();

        // for back tracing
        Map<Node, Double> distanceMemory = new HashMap<>();
        Map<Node, Node> parentMemory = new LinkedHashMap<>();

        open.add(new Node(current, 0));
        Node destNode = new Node(dest, 0);

        // while there is a node to check in open list
        Node u = null;
        while (!open.isEmpty()) {

            u = open.poll(); // poll the closest one
            closed.add(u); // put it in closed list, to not check anymore

            // if u is destination, break;
            if (u.getCoord().equals(dest)) {
                destNode = u;
                break;
            }

            for (Coord c : getAdjacentCoordinates(u.getCoord(), scanMapTiles, current)) {
                // if this node hasn't already been checked
                if (!closed.contains(new Node(c, 0)) && globalMap.get(c) != null && validateTile(globalMap.get(c), drive)) {

                    // TODO: MAYBE: assess cost depending on the tile's terrain, science, etc
                    double g = u.getData() + 1; // each move cost is 1, for now
                    double h = getDistance(c, dest); // distance from neighbor to destination
                    double f = h + g; // total heuristic of this neighbor c
                    Node n = new Node(c, f);

                    // for back tracing, store in hashmap
                    if (distanceMemory.containsKey(n)) {

                        // if distance of this neighboring node is less than memory, update
                        // else, leave as it is
                        if (distanceMemory.get(n) > f) {
                            distanceMemory.put(n, f);
                            open.remove(n);  // also update from open list
                            open.add(n);
                            parentMemory.put(n, u); // add in parent
                        }


                    } else {
                        // if this neighbor node is new, then add to memory
                        distanceMemory.put(n, f);
                        parentMemory.put(n, u);
                        open.add(n);
                    }

                }

            }

        }

        List<String> moves = getTrace(destNode, parentMemory);
        return moves;
    }

    private List<String> getTrace(Node dest, Map<Node, Node> parents) {
        Node backTrack = dest;
        double mindist = Double.MAX_VALUE;
        for (Node n : parents.keySet()) {
            if (n.equals(dest)) {
                backTrack = dest;
                break;
            } else {
                double distance = getDistance(dest.getCoord(), n.getCoord());
                if (distance < mindist) {
                    mindist = distance;
                    backTrack = n;
                }

            }
        }

        List<String> moves = new ArrayList<>();

        while (backTrack != null) {
            Node parent = parents.get(backTrack);
            if (parent != null) {
                int parentX = parent.getCoord().xpos;
                int parentY = parent.getCoord().ypos;
                int currentX = backTrack.getCoord().xpos;
                int currentY = backTrack.getCoord().ypos;
                if (currentX == parentX) {
                    if (parentY < currentY) {
                        moves.add(0, "S");
                    } else {
                        moves.add(0, "N");
                    }

                } else {
                    if (parentX < currentX) {
                        moves.add(0, "E");
                    } else {
                        moves.add(0, "W");
                    }
                }
            }
            backTrack = parent;

        }
        return moves;
    }

    // to check neighbors for heuristics
    public List<Coord> getAdjacentCoordinates(Coord coord, MapTile[][] scanMapTiles, Coord current) {
        List<Coord> list = new ArrayList<>();

        // coordinates
        int west = coord.xpos - 1;
        int east = coord.xpos + 1;
        int north = coord.ypos - 1;
        int south = coord.ypos + 1;

        Coord s = new Coord(coord.xpos, south); // S
        Coord e = new Coord(east, coord.ypos); // E
        Coord w = new Coord(west, coord.ypos); // W
        Coord n = new Coord(coord.xpos, north); // N

        list.add(e);
        list.add(w);
        list.add(s);
        list.add(n);

        return list;
    }

    public static double getDistance(Coord current, Coord dest) {
        double dx = current.xpos - dest.xpos;
        double dy = current.ypos - dest.ypos;
        return Math.sqrt((dx * dx) + (dy * dy)) * 100;
    }

    public boolean validateTile(MapTile maptile, RoverDriveType drive) {
//        System.out.println("hasrover: " + maptile.getHasRover() + ", terrain: " + maptile.getTerrain());
        Terrain terrain = maptile.getTerrain();
        boolean hasRover = maptile.getHasRover();

        if (hasRover || terrain == Terrain.NONE) {
            return false;
        }

        if (terrain == Terrain.SAND) {
            if (drive == RoverDriveType.WALKER || drive == RoverDriveType.WHEELS) return false;
        }

        if (terrain == Terrain.ROCK) {
            if (drive == RoverDriveType.TREADS || drive == RoverDriveType.WHEELS) return false;
        }
        return true;
    }

    public boolean targetVisible(Coord currentLoc, Coord target){
        int dx = Math.abs(currentLoc.xpos - target.xpos);
        int dy = Math.abs(currentLoc.ypos - target.ypos);
        if (dx <= 3 && dy <= 3) return true;
        return false;
    }


}
