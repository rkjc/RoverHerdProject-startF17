package MapSupport;

import java.util.HashMap;
import java.util.Map.Entry;

import enums.RoverConfiguration;
import enums.Science;

public class RoverLocations {
	
	private HashMap<RoverConfiguration, Coord> roverHash;

	public RoverLocations(){
		roverHash = new HashMap<RoverConfiguration, Coord>();
	}
	
	public RoverLocations(HashMap<RoverConfiguration, Coord> rovHash){
		roverHash = (HashMap<RoverConfiguration, Coord>) rovHash.clone();
	}
	

	
	public synchronized boolean moveRover(RoverConfiguration rname, Coord loc){
		if(roverHash.containsValue(loc)){
			return false;
		}			
		roverHash.put(rname,  loc);			
		return true;
	}
	
	public synchronized Coord getLocation(RoverConfiguration rname){
		return roverHash.get(rname);
	}
	
	public synchronized RoverConfiguration getName(Coord loc){
		for (Entry<RoverConfiguration, Coord> entry : roverHash.entrySet()) {
            if (entry.getValue().equals(loc)) {
                return entry.getKey();
            }
        }	
		return null;
	}
	
	public synchronized boolean containsCoord (Coord loc){
		return roverHash.containsValue(loc);
	}
	
	public void printRovers(){
		for(RoverConfiguration rovloc : roverHash.keySet()){
			String key = rovloc.toString();
            String value = roverHash.get(rovloc).toString();  
            System.out.println(key + " " + value);  
    	}
	}
	
	public synchronized HashMap<RoverConfiguration, Coord> getHashMapClone(){	
		return (HashMap<RoverConfiguration, Coord>) roverHash.clone();
	}
	
	public synchronized void putRover(RoverConfiguration rname, Coord rloc){	
		roverHash.put(rname, rloc);
	}
	
	public RoverLocations clone(){
		return new RoverLocations(this.roverHash);
	}
	
	
	
	/*
	 * These are only used for testing and development
	 */
	
	public void loadExampleTestRoverLocations(){
        // place all the rovers into the map in their initial positions
        // TODO - have initial positions loaded from a file instead of hard coded
		roverHash.put(RoverConfiguration.ROVER_01, new Coord(1,1));
		roverHash.put(RoverConfiguration.ROVER_02, new Coord(2,1));
		roverHash.put(RoverConfiguration.ROVER_03, new Coord(3,1));
		roverHash.put(RoverConfiguration.ROVER_04, new Coord(4,1));
		roverHash.put(RoverConfiguration.ROVER_05, new Coord(5,1));
		roverHash.put(RoverConfiguration.ROVER_06, new Coord(6,1));
		roverHash.put(RoverConfiguration.ROVER_07, new Coord(7,1));
		roverHash.put(RoverConfiguration.ROVER_08, new Coord(8,1));
		roverHash.put(RoverConfiguration.ROVER_09, new Coord(9,1));
		roverHash.put(RoverConfiguration.ROVER_10, new Coord(10,1));
		roverHash.put(RoverConfiguration.ROVER_11, new Coord(11,1));
		roverHash.put(RoverConfiguration.ROVER_12, new Coord(12,1));
		roverHash.put(RoverConfiguration.ROVER_13, new Coord(13,1));
		roverHash.put(RoverConfiguration.ROVER_14, new Coord(14,1));
		roverHash.put(RoverConfiguration.ROVER_15, new Coord(15,1));
		roverHash.put(RoverConfiguration.ROVER_16, new Coord(16,1));
		roverHash.put(RoverConfiguration.ROVER_17, new Coord(17,1));
		roverHash.put(RoverConfiguration.ROVER_18, new Coord(18,1));
		roverHash.put(RoverConfiguration.ROVER_19, new Coord(19,1));
		roverHash.put(RoverConfiguration.ROVER_20, new Coord(20,1));
		
		// test rovers
		roverHash.put(RoverConfiguration.ROVER_00, new Coord(4,20));
		roverHash.put(RoverConfiguration.ROVER_99, new Coord(4,7));
	}
	
	public void loadSmallExampleTestRoverLocations(){
		
		// test rovers
		roverHash.put(RoverConfiguration.ROVER_00, new Coord(4,20));
		roverHash.put(RoverConfiguration.ROVER_99, new Coord(4,7));
	}
}
