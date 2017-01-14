package rover_logic;

import java.io.Serializable;
import java.util.*;
import common.Coord;
import common.MapTile;
import enums.RoverDriveType;
import enums.Terrain;

//*Original code implemented by Daniel Beard at
//*https://github.com/daniel-beard/DStarLiteJava/blob/master/DStarLite.java
//*Thank you!
//*Code modified for swarmBot purposes

public class DStarLite implements Serializable {

	private static final long serialVersionUID = 1L;

	// Private Member variables
	private List<State> path = new ArrayList<State>();
	private double C1;
	private final int OBSTACLE_COST = 10000;
	private double k_m;
	private State s_start = new State();
	private State s_goal = new State();
	public Coord start_c, goal_c;
	private State s_last = new State();
	private int maxSteps;
	public int scanElem = 0;
	public boolean verChanged = false;
	// private boolean isWaiting; //will wait until rover_notifies me.
	private PriorityQueue<State> openList = new PriorityQueue<State>();
	// Change back to private****
	public HashMap<State, CellInfo> cellHash = new HashMap<State, CellInfo>();
	// private HashMap<State, Float> openHash = new HashMap<State, Float>();

	// Constants
	private double M_SQRT2 = Math.sqrt(2.0);

	// Rover-Specific considerations
	RoverDriveType rdt;

	// Default constructor
	public DStarLite() {
		maxSteps = 10000; // how many steps to update the map with
		C1 = 1; // cost constant
	}

	public DStarLite(RoverDriveType rdt) {
		maxSteps = 10000;
		C1 = 1;
		this.rdt = rdt;
	}

	// Calculate Keys
	public void CalculateKeys() {

	}

	/*
	 * Initialise Method
	 * 
	 * @params start and goal coordinates
	 */
	public void init(Coord start, Coord goal) {
		cellHash.clear();
		path.clear();
		// openHash.clear();
		while (!openList.isEmpty())
			openList.poll();

		k_m = 0;

		s_start.setCoord(start);
		s_goal.setCoord(goal);

		// rhs, g, and cost to goal should all be low, or zero
		// H is cost from currentVertex to original vertex.
		// g is the cost from currentVertex to goal //using manhattan distance
		CellInfo tmp = new CellInfo();
		tmp.g = Double.POSITIVE_INFINITY;
		tmp.rhs = 0;
		tmp.cost = C1;
		// add it to hash table
		cellHash.put(s_goal, tmp);
		openList.add(s_goal);
		/*
		 * the start of cell's cost and rhs value will be manhattan distance to
		 * goal. Recall, algorithm finds cell path...backwards, i.e heuristic
		 * costs should decrease moving forward.
		 */
		tmp = new CellInfo();
		tmp.g = tmp.rhs = heuristicManhattan(s_start, s_goal);
		tmp.cost = C1;
		cellHash.put(s_start, tmp);
		s_start = calculateKey(s_start);

		s_last = s_start;
		// System.out.println("s_last in init is s_start: " +
		// s_start.toString());

	}

	/*
	 * CalculateKey(state u) As per [S. Koenig, 2002]
	 */
	private State calculateKey(State u) {
		double val = Math.min(getRHS(u), getG(u));

		u.getK().setFirst(val + heuristicManhattan(u, s_start) + k_m);
		u.getK().setSecond(val); // tie breaker when choosing nodes/states

		return u;
	}

	/*
	 * Returns the rhs value for state u.
	 */
	private double getRHS(State u) {
		if (u == s_goal)
			return 0;

		// if the cellHash doesn't contain the State u
		if (cellHash.get(u) == null) {
			return heuristicManhattan(u, s_goal);
			// return Double.POSITIVE_INFINITY;
		}
		return cellHash.get(u).rhs;
	}

	/*
	 * Returns the g value for the state u.
	 */
	private double getG(State u) {
		// if the cellHash doesn't contain the State u, use Manhattan distance
		if (cellHash.get(u) == null) {
			return heuristicManhattan(u, s_goal);
			// return Double.POSITIVE_INFINITY;
		}
		return cellHash.get(u).g;
	}

	/*
	 * Pretty self explanatory, the heuristic we use is the 8-way distance
	 * scaled by a constant C1 (should be set to <= min cost)
	 */
	@SuppressWarnings("unused")
	private double heuristic(State a, State b) {
		return eightCondist(a, b) * C1;
	}

	// better for four way graph system
	private double heuristicManhattan(State a, State b) {
		double h = Math.abs(a.getCoord().xpos - b.getCoord().xpos) + Math.abs(a.getCoord().ypos - b.getCoord().ypos);
		return h;
	}

	/*
	 * Returns the 8-way distance between state a and state b
	 */

	private double eightCondist(State a, State b) {
		double temp;
		double min = Math.abs(a.getCoord().xpos - b.getCoord().xpos);
		double max = Math.abs(a.getCoord().ypos - b.getCoord().ypos);
		if (min > max) {
			temp = min;
			min = max;
			max = temp;
		}
		return ((M_SQRT2 - 1.0) * min + max);

	}

	public boolean replan() {
		path.clear();
		// s_last = s_start;
		// like A* - finds the shortest path using priority queue - returns -1
		// if no path/exceeds max steps
		// init(start_c, goal_c);
		int res = computeShortestPath();
		if (res < 0) {
			System.out.println("No Path to Goal : shortest_path returns -1");
			return false;
		}

		LinkedList<State> n = new LinkedList<State>();
		State cur = s_start;

		if (getG(s_start) == Double.POSITIVE_INFINITY) {
			System.out.println("No Path to Goal : s_start == POS.INF");
			return false;
		}

		while (cur.neq(s_goal)) {
			//System.out.println("CURRENT IS " + cur.toString());
			path.add(cur);
			//System.out.println("Added to path: " + cur.toString());
			n = new LinkedList<State>();
			n = getSucc(cur);

			if (n.isEmpty()) {
				System.out.println("No Path to Goal : n is empty (get successors)");
				return false;
			}

			// check all succesors
			// find the one with smallest cost and set it as start
			double cmin = Double.POSITIVE_INFINITY;
			double tmin = 0;
			State smin = new State();

			for (State i : n) {
				double val = calculateCost(s_start, i); // Changed cost here to
				// account for obstacles
				double val2 = trueDist(i, s_goal) + trueDist(cur, i);
				val += getG(i);

				// if cost is close to pos_inf or previous cmin,
				// use actual distance, and update min if min distance is less
				// than tmin
				if (close(val, cmin)) {
					if (tmin > val2) {
						tmin = val2;
						cmin = val;
						smin = i;
						// System.out.println("new start will be " +
						// smin.toString());
					}
				} else if (val < cmin) { // else if val is less than previous
											// min, update new min
					tmin = val2;
					cmin = val;
					smin = i;
					// System.out.println("new start will be " +
					// smin.toString());
				}
			}

			n.clear();
			cur = smin;
			// System.out.println("NEW S_START IS: " + cur.toString());
			// // cur = smin;
			// TODO: make rover move, let rover do stuff, wait in the mean time
			// when rover signals, start again
		}
		path.add(s_goal);
		return true;
	}

	/*
	 * As per [S. Koenig,2002] except for two main modifications: 1. We stop
	 * planning after a number of steps, 'maxsteps' we do this because this
	 * algorithm can plan forever if the start is surrounded by obstacles 2. We
	 * lazily remove states from the open list so we never have to iterate
	 * through it.
	 */
	private int computeShortestPath() {
		LinkedList<State> s = new LinkedList<State>();

		if (openList.isEmpty()) {
			// System.out.println("open list is empty, returning 1 from CSP");
			return 1;
		}

		int k = 0;
		while ((!openList.isEmpty())
				&& (openList.peek().lt(s_start = calculateKey(s_start)) || (getRHS(s_start) != getG(s_start)))) {// inconsistent
																													// state
			// System.out.println("Enterring while loop in CSP");
			if (k++ > maxSteps) {
				System.out.println("At maxsteps");
				return -1;
			}

			State u = openList.poll();

			State k_old = new State(u);
			// System.out.println("Lookint at state: " + u);
			if (k_old.lt(calculateKey(u))) { // u is out of date
				// System.out.println("Old key is less than new key");
				// System.out.println("Inserting to priority key " +
				// u.toString());
				insert(u);
			} else if (getG(u) > getRHS(u)) { // needs update (got better)
				setG(u, getRHS(u));
				s = getPred(u);
				// System.out.println("g > rhs, obstacle moved");
				for (State i : s) {
					updateVertex(i);
				}
			} else {
				setG(u, Double.POSITIVE_INFINITY);
				// System.out.println("last branch, setting g to inf...updating
				// vertices...");
				s = getPred(u);

				for (State i : s) {
					updateVertex(i);
				}
				updateVertex(u);
			}
		} // while
		return 0;
	}

	/*
	 * Returns a list of successor states for state u, since this is an 8-way
	 * graph this list contains all of a cells neighbours. Unless the cell is
	 * occupied, in which case it has no successors.
	 */

	private LinkedList<State> getSucc(State u) {
		LinkedList<State> s = new LinkedList<State>();
		State tempState;

		if (occupied(u))
			return s;

		// Generate the successors, starting at the immediate right,
		// Moving in a clockwise manner
		// Set all kesy to -1 for now.

		// EAST
		tempState = new State(u.getCoord().xpos + 1, u.getCoord().ypos, new Pair<Double, Double>(-1.0, -1.0));
		s.addFirst(tempState);
		// SOUTH
		tempState = new State(u.getCoord().xpos, u.getCoord().ypos + 1, new Pair<Double, Double>(-1.0, -1.0));
		s.addFirst(tempState);
		// WEST
		tempState = new State(u.getCoord().xpos - 1, u.getCoord().ypos, new Pair<Double, Double>(-1.0, -1.0));
		s.addFirst(tempState);
		// NORTH
		tempState = new State(u.getCoord().xpos, u.getCoord().ypos - 1, new Pair<Double, Double>(-1.0, -1.0));
		s.addFirst(tempState);

		return s;
	}

	/*
	 * Returns a list of all the predecessor states for state u. Since this is
	 * for an 4-way connected graph, the list contains all the neighbours for
	 * state u. Occupied neighbours are not added to the list
	 */
	private LinkedList<State> getPred(State u) {
		LinkedList<State> s = new LinkedList<State>();
		State tempState;

		// EAST
		tempState = new State(u.getCoord().xpos + 1, u.getCoord().ypos, new Pair<Double, Double>(-1.0, -1.0));
		if (!occupied(tempState))
			s.addFirst(tempState);
		// SOUTH
		tempState = new State(u.getCoord().xpos, u.getCoord().ypos + 1, new Pair<Double, Double>(-1.0, -1.0));
		if (!occupied(tempState))
			s.addFirst(tempState);
		// WEST
		tempState = new State(u.getCoord().xpos - 1, u.getCoord().ypos, new Pair<Double, Double>(-1.0, -1.0));
		if (!occupied(tempState))
			s.addFirst(tempState);
		// NORTH
		tempState = new State(u.getCoord().xpos, u.getCoord().ypos - 1, new Pair<Double, Double>(-1.0, -1.0));
		if (!occupied(tempState)) {
			s.addFirst(tempState);
		}
		return s;
	}

	/*
	 * Update the position of the agent/robot. This does not force a replan.
	 */
	public void updateStart(Coord coord) {
		s_start.setCoord(coord);

		k_m += heuristicManhattan(s_last, s_start);

		s_start = calculateKey(s_start);
		s_last = s_start;
	}

	/*
	 * This is somewhat of a hack, to change the position of the goal we first
	 * save all of the non-empty nodes on the map, clear the map, move the goal
	 * and add re-add all of the non-empty cells. Since most of these cells are
	 * not between the start and goal this does not seem to hurt performance too
	 * much. Also, it frees up a good deal of memory we are probably not going
	 * to use.
	 */
	// @SuppressWarnings("unchecked")
	public void updateGoal(Coord coord) // ****************************************************************************
	{
		List<Pair<ipoint2, Double>> toAdd = new ArrayList<Pair<ipoint2, Double>>();
		Pair<ipoint2, Double> tempPoint;

		for (Map.Entry<State, CellInfo> entry : cellHash.entrySet()) {
			if (!close(entry.getValue().cost, C1)) {
				tempPoint = new Pair<ipoint2, Double>(
						new ipoint2(entry.getKey().getCoord().xpos, entry.getKey().getCoord().ypos),
						entry.getValue().cost);
				toAdd.add(tempPoint);
			}
		}

		cellHash.clear();
		// openHash.clear();

		while (!openList.isEmpty())
			openList.poll();

		k_m = 0;

		s_goal.setCoord(coord);

		CellInfo tmp = new CellInfo();
		tmp.g = tmp.rhs = 0;
		tmp.cost = C1;

		cellHash.put(s_goal, tmp);
		// * Double check new cell here, should actual costs be evaluated?
		tmp = new CellInfo();
		tmp.g = tmp.rhs = heuristicManhattan(s_start, s_goal);
		tmp.cost = C1;
		cellHash.put(s_start, tmp);
		s_start = calculateKey(s_start);

		s_last = s_start;

		Iterator<Pair<ipoint2, Double>> iterator = toAdd.iterator();
		while (iterator.hasNext()) {
			tempPoint = iterator.next();
			updateCell(tempPoint.first().x, tempPoint.first().y, tempPoint.second());
		}

	}

	/*
	 * As per [S. Koenig, 2002]
	 */
	private void updateVertex(State u) {
		LinkedList<State> s = new LinkedList<State>();

		if (u.neq(s_goal)) {
			s = getSucc(u);
			double tmp = Double.POSITIVE_INFINITY;
			double tmp2;

			for (State i : s) {
				tmp2 = getG(i) + calculateCost(u, i); // cost now takes into
														// account obstacles.
				if (tmp2 < tmp)
					tmp = tmp2;
			}
			// if (!close(getRHS(u), tmp))
			setRHS(u, tmp);
			// System.out.println("Updating vertex, new RHS value: " + tmp);
		}
		if (openList.contains(u)) {
			openList.remove(u);
			// System.out.println("removed " + u.toString() + " from openList.
			// Updating Vertex");
		}
		if (!close(getG(u), getRHS(u)))
			insert(u);
	}

	/*
	 * Sets the G value for state u
	 */
	private void setG(State u, double g) {
		makeNewCell(u);
		cellHash.get(u).g = g;
	}

	/*
	 * Sets the rhs value for state u
	 */
	private void setRHS(State u, double rhs) {
		makeNewCell(u);
		cellHash.get(u).rhs = rhs;
	}

	/*
	 * Checks if a cell is in the hash table, if not it adds it in. all new
	 * cells have temporary C1 costs and Manhattan g/heuristic values.
	 */
	private void makeNewCell(State u) {
		// TODO: if it's in hash cell check to make sure mt is updated,
		// otherwise it might crash
		if (cellHash.get(u) != null) {
			return;
		}
		CellInfo tmp = new CellInfo();
		tmp.g = tmp.rhs = heuristicManhattan(u, s_goal);
		tmp.cost = C1;
		cellHash.put(u, tmp);
	}

	/*
	 * updateCell as per [S. Koenig, 2002]
	 */
	public void updateCell(int x, int y, double val) {
		State u = new State();
		u.setCoord(new Coord(x, y));

		if ((u.eq(s_start)) || (u.eq(s_goal)))
			return;

		makeNewCell(u);
		cellHash.get(u).cost = val;
		updateVertex(u);
	}

	/*
	 * updateCell, given a mapTile to calculate new cost
	 */
	public void updateCell(Coord cellCoordinate, MapTile mt) {
		// scanElem++;
		State u = new State();
		u.setCoord(cellCoordinate);
		u.setMapTile(mt);
		// don't update start or target cell?
		if ((u.eq(s_start)) || (u.eq(s_goal)))
			return;

		makeNewCell(u);
		double oldCost = cellHash.get(u).cost;
		double newCost = isObstacle(mt) ? OBSTACLE_COST : C1;

		if (oldCost != newCost) {
			verChanged = true;
			if (isObstacle(mt))
				cellHash.get(u).cost = OBSTACLE_COST;
			else
				cellHash.get(u).cost = C1;
			updateVertex(u);
		}

		if (scanElem == 4) {
			if (verChanged) {
				k_m += heuristicManhattan(s_last, s_start);
				s_last = s_start;
			}
			scanElem = 0;
			verChanged = false;
			// System.out.println("Updated last cell...");
			// System.out.println("New s_last is now s_start: " +
			// s_last.toString());
		}

	}

	/*
	 * Inserts state u into openList and openHash
	 */
	private void insert(State u) {
		u = calculateKey(u);
		openList.add(u);
	}

	/*
	 * Returns true if the cell is occupied (non-traversable), false otherwise.
	 * Non-traversable are marked with a cost < 0
	 */
	private boolean occupied(State u) {
		// if the cellHash does not contain the State u
		if (cellHash.get(u) == null)
			return false;
		return (cellHash.get(u).cost >= OBSTACLE_COST);
	}

	/*
	 * Euclidean cost between state a and state b
	 */
	private double trueDist(State a, State b) {
		float x = a.getCoord().xpos - b.getCoord().xpos;
		float y = a.getCoord().ypos - b.getCoord().ypos;
		return Math.sqrt(x * x + y * y);
	}

	/*
	 * Returns the cost of moving from state a to state b. This could be either
	 * the cost of moving off state a or onto state b, we went with the former.
	 * This is also the 8-way cost.
	 */
	@SuppressWarnings("unused")
	private double cost(State a, State b) {
		int xd = Math.abs(a.getCoord().xpos - b.getCoord().xpos);
		int yd = Math.abs(a.getCoord().ypos - b.getCoord().ypos);
		double scale = 1;

		if (xd + yd > 1)
			scale = M_SQRT2;

		if (cellHash.containsKey(a) == false)
			return scale * C1;
		return scale * cellHash.get(a).cost;
	}

	// this method gives the cost of moving from state a to state b
	private double calculateCost(State a, State b) {
		// Return cost at each if/else instead of accumulator
		// double accum = 0;
		if (b == s_goal)
			return 0;
		// if there's an obstacle, big cost, else cost is 1
		if (isObstacle(b.mapTile))
			return OBSTACLE_COST;
		else
			return C1;
	}

	// true if there's an obstacle i.e another rover, wrong terrain type...
	private boolean isObstacle(MapTile mt) {
		boolean obstacle = false;
		// TODO: double check this
		if (mt == null)
			return false;

		if (mt.getHasRover()) {
			obstacle = true;
		} else if (mt.getTerrain() == Terrain.NONE) {
			obstacle = true;
		} else if (rdt == RoverDriveType.WHEELS) {
			if (mt.getTerrain() == Terrain.SAND || mt.getTerrain() == Terrain.ROCK)
				obstacle = true;
		} else if (rdt == RoverDriveType.TREADS) {
			if (mt.getTerrain() == Terrain.ROCK)
				obstacle = true;
		} else if (rdt == RoverDriveType.WALKER) {
			if (mt.getTerrain() == Terrain.SAND)
				obstacle = true;
		}
		return obstacle;
	}

	/*
	 * Returns true if x and y are within 10E-5, false otherwise
	 */
	private boolean close(double x, double y) {
		if (x == Double.POSITIVE_INFINITY && y == Double.POSITIVE_INFINITY)
			return true;
		return (Math.abs(x - y) < 0.00001);
	}

	public List<State> getPath() {
		return path;
	}

	public static void main(String[] args) {
		DStarLite pf = new DStarLite();
		pf.init(new Coord(1, 1), new Coord(40, 60));
		pf.updateCell(2, 1, -1);
		pf.updateCell(2, 0, -1);
		pf.updateCell(2, 2, -1);
		pf.updateCell(3, 0, -1);

		//System.out.println("Start node: (0,1)");
		//System.out.println("End node: (3,1)");

		// Time the replanning
		long begin = System.currentTimeMillis();
		pf.replan();
		// pf.updateGoal(3, 2);
		long end = System.currentTimeMillis();

		System.out.println("Time: " + (end - begin) + "ms");

		List<State> path = pf.getPath();
		for (State i : path) {
			System.out.println("x: " + i.getCoord().xpos + " y: " + i.getCoord().ypos);
		}

	}

}

class CellInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public double g = 0;
	public double rhs = 0;
	public double cost = 0;
}

class ipoint2 {
	public int x = 0;
	public int y = 0;

	// default constructor
	public ipoint2() {

	}

	// overloaded constructor
	public ipoint2(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
