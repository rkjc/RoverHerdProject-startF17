package rover_logic;

import common.Coord;
import common.MapTile;

//this class also taken from //* https://github.com/daniel-beard/DStarLiteJava/
//but slightly modified to fit rover game

public class State implements Comparable<Object> {
	private Coord coord;
	public MapTile mapTile;
	private Pair<Double, Double> k = new Pair<Double, Double>(0.0, 0.0);

	public State() {
	}
	
	public State(int x, int y, Pair<Double, Double> k){
		this.coord = new Coord(x, y);
		//this.data = data;
		this.k = k;
	}

	public State(Coord coord, MapTile mt, Pair<Double, Double> k) {
		this.coord = coord;
		this.mapTile = mt;
		this.k = k;
	}

	public State(State other) {
		this.coord = other.coord;
		this.mapTile = other.mapTile;
		this.k = other.k;
	}

	// Equals
	public boolean eq(final State s2) {
		return ((this.coord.xpos == s2.coord.xpos) && (this.coord.ypos == s2.coord.ypos));
	}

	// Not Equals
	public boolean neq(final State s2) {
		return ((this.coord.xpos != s2.coord.xpos) || (this.coord.ypos != s2.coord.ypos));
	}

	// comparing states by their keys
	// Greater than
	public boolean gt(final State s2) {
		if (k.first() - 0.00001 > s2.k.first())
			return true;
		else if (k.first() < s2.k.first() - 0.00001)
			return false;
		return k.second() > s2.k.second();
	}

	// Less than or equal to
	public boolean lte(final State s2) {
		if (k.first() < s2.k.first())
			return true;
		else if (k.first() > s2.k.first())
			return false;
		return k.second() < s2.k.second() + 0.00001;
	}

	// Less than
	public boolean lt(final State s2) {
		if (k.first() + 0.000001 < s2.k.first())
			return true;
		else if (k.first() - 0.000001 > s2.k.first())
			return false;
		return k.second() < s2.k.second();
	}

	// CompareTo Method. This is necessary when this class is used in a priority
	// queue
	public int compareTo(Object that) {
		// This is a modified version of the gt method
		State other = (State) that;
		if (k.first() - 0.00001 > other.k.first())
			return 1;
		else if (k.first() < other.k.first() - 0.00001)
			return -1;
		if (k.second() > other.k.second())
			return 1;
		else if (k.second() < other.k.second())
			return -1;
		return 0;
	}

	// Override the CompareTo function for the HashMap usage
	@Override
	public int hashCode() {
		return this.coord.hashCode();
	}

	public Coord getCoord() {
		return coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	public MapTile getMapTile() {
		return mapTile;
	}

	public void setMapTile(MapTile mapTile) {
		this.mapTile = mapTile;
	}

	public Pair<Double, Double> getK() {
		return k;
	}

	public void setK(Pair<Double, Double> k) {
		this.k = k;
	}

	@Override
	public boolean equals(Object aThat) {
		// check for self-comparison
		if (this == aThat)
			return true;

		// use instanceof instead of getClass here for two reasons
		// 1. if need be, it can match any supertype, and not just one class;
		// 2. it renders an explict check for "that == null" redundant, since
		// it does the check for null already - "null instanceof [type]" always
		// returns false. (See Effective Java by Joshua Bloch.)
		if (!(aThat instanceof State))
			return false;
		// Alternative to the above line :
		// if ( aThat == null || aThat.getClass() != this.getClass() ) return
		// false;

		// cast to native object is now safe
		State that = (State) aThat;

		// now a proper field-by-field evaluation can be made
		if (this.coord.xpos == that.coord.xpos && this.coord.ypos == that.coord.ypos)
			return true;
		return false;
	}

	public String toString() {
		String str = "";
		str += "coord: " + coord;
		return str;
	}

}
