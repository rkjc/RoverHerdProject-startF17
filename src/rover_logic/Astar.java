package rover_logic;
import enums.Science;
import enums.Terrain;
import enums.RoverToolType;
import enums.RoverDriveType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Communication;
import common.Coord;
import common.MapTile;
import common.PlanetMap;
import common.ScanMap;



public class Astar extends PlanetMap
{
    protected boolean[][][] explored;
    String url = "http://localhost:3000/api";
    String corp_secret = "gz5YhL70a2";
    String rovername = "ROVER_03";
    Map<Coord, MapTile> globalMap = new HashMap<Coord, MapTile>();
    //only search up to known map
    // int maxKnownX = 100;
    // int maxKnownY = 100;

	protected Communication com = new Communication(url, rovername, corp_secret);
    
    
	private static class MyRunnable implements Runnable
	{
	
    private final Communication com;
	private final Coord centerpos;
	private final MapTile[][] mapArray;
		
		
		
		MyRunnable(final Communication com, final Coord centerpos, final MapTile[][] mapArray) {
			this.com = com;
			this.centerpos = centerpos;
			this.mapArray = mapArray;
		}
		public void run() {
			com.postScanMapTiles(centerpos, mapArray);
		}
	}
	public Astar() {
        this(1000,1000);//this is a risky assumption, we should be cautious about it.
    }
    public Astar(int width, int height) {
        this(width,height,null,null);
    }
    public Astar(int width, int height, Coord startPos, Coord targetPos) {
        super(width, height, startPos, targetPos); 
        explored = new boolean[width][height][5];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                for(int k = 0; k < 5; k++) {
                    explored[i][j][k] = false;
                }
            }
        }
		
    }
    //adds a scanmap to the Astar.
    public void addScanMap(ScanMap scan, Coord centerpos, RoverToolType tool1, RoverToolType tool2) {
        MapTile[][] mapArray = scan.getScanMap();
		new Thread(new MyRunnable(com, centerpos, mapArray)).start();
		boolean[] mask = new boolean[5];
        mask[0] = true;
        mask[1] = tool1 == RoverToolType.RADIATION_SENSOR || tool2 == RoverToolType.RADIATION_SENSOR ?  true : false;
        mask[2] = tool1 == RoverToolType.CHEMICAL_SENSOR || tool2 == RoverToolType.CHEMICAL_SENSOR ? true : false;
        mask[3] = tool1 == RoverToolType.SPECTRAL_SENSOR || tool2 == RoverToolType.SPECTRAL_SENSOR ? true : false;
        mask[4] = tool1 == RoverToolType.RADAR_SENSOR || tool2 == RoverToolType.RADAR_SENSOR ? true : false;
        for(int i = 0; i < mapArray.length; i++) {
            for(int j = 0; j < mapArray[i].length; j++) {
                //If we're inbounds
                if(i-(scan.getEdgeSize()/2)+centerpos.xpos >= 0 && j-(scan.getEdgeSize()/2)+centerpos.ypos >= 0) {
                    boolean[] tileExplored = explored[i-(scan.getEdgeSize()/2)+centerpos.xpos][j-(scan.getEdgeSize()/2)+centerpos.ypos];
                    Science oldScience = this.getTile(i-(scan.getEdgeSize()/2)+centerpos.xpos, j-(scan.getEdgeSize()/2)+centerpos.ypos).getScience();
                    if((mask[0] && !tileExplored[0]) || mapArray[i][j].getScience() != Science.NONE || (mask[1] && oldScience == Science.RADIOACTIVE) || (mask[2] && oldScience == Science.ORGANIC) || (oldScience == Science.MINERAL) || (mask[4] && oldScience == Science.CRYSTAL)) {
                        this.setTile(mapArray[i][j].getCopyOfMapTile(), i-(scan.getEdgeSize()/2)+centerpos.xpos, j-(scan.getEdgeSize()/2)+centerpos.ypos);
                        Coord tempC = new Coord(i-(scan.getEdgeSize()/2)+centerpos.xpos, j-(scan.getEdgeSize()/2)+centerpos.ypos);
                        if(globalMap.get(tempC) == null){
                            globalMap.put(tempC, mapArray[i][j].getCopyOfMapTile());
                        }else{
                        	//else make sure to replace the tile, and not add another value to it. 
                            globalMap.replace(tempC, globalMap.get(tempC), mapArray[i][j].getCopyOfMapTile());
                        }
                    }
                    for(int m = 0; m < 5; m++) { //mask the explored array with our mask, to show we've covered such and such sensors.
                        explored[i-(scan.getEdgeSize()/2)+centerpos.xpos][j-(scan.getEdgeSize()/2)+centerpos.ypos][m] = mask[m] || explored[i-(scan.getEdgeSize()/2)+centerpos.xpos][j-(scan.getEdgeSize()/2)+centerpos.ypos][m];
                    }
                }
            }
        }
    }
    //Counts how many squares will be revealed by a rover at the given post with the given tools.
    public int revealCount(Coord pos, RoverToolType tool1, RoverToolType tool2) {
        int result = 0;
        int range = 7;
        if(tool1 == RoverToolType.RANGE_BOOTER || tool2 == RoverToolType.RANGE_BOOTER) { range = 11; }
        boolean[] mask = new boolean[5];
        mask[0] = true;
        mask[1] = tool1 == RoverToolType.RADIATION_SENSOR || tool2 == RoverToolType.RADIATION_SENSOR ?  true : false;
        mask[2] = tool1 == RoverToolType.CHEMICAL_SENSOR || tool2 == RoverToolType.CHEMICAL_SENSOR ? true : false;
        mask[3] = tool1 == RoverToolType.SPECTRAL_SENSOR || tool2 == RoverToolType.SPECTRAL_SENSOR ? true : false;
        mask[4] = tool1 == RoverToolType.RADAR_SENSOR || tool2 == RoverToolType.RADAR_SENSOR ? true : false;
        for(int i = pos.xpos-(range/2); i <= pos.xpos+(range/2); i++) {
            for(int j = pos.ypos-(range/2); j <= pos.ypos+(range/2); j++) {
                for(int k = 0; k < 5; k++) {
                    if(i >= 0 && j >= 0 && mask[k] && !explored[i][j][k]) {
                        result++;
                    }
                }
            }
        }
        return result;
    }
    //"tries" all four cardinal directions and prints out how much would be revealed
    public void debugPrintRevealCounts(Coord pos, RoverToolType tool1, RoverToolType tool2) {
        System.out.println("N: " + Integer.toString(this.revealCount(new Coord(pos.xpos, pos.ypos-1), tool1, tool2)));
        System.out.println("E: " + Integer.toString(this.revealCount(new Coord(pos.xpos+1, pos.ypos), tool1, tool2)));
        System.out.println("S: " + Integer.toString(this.revealCount(new Coord(pos.xpos, pos.ypos+1), tool1, tool2)));
        System.out.println("W: " + Integer.toString(this.revealCount(new Coord(pos.xpos-1, pos.ypos), tool1, tool2)));
    }
    //A* pathfinder
    public char findPath(Coord start, Coord dest, RoverDriveType drive) {
    	// ##### If destination coordinate is blocked/unreachable, return U
        if (blocked(dest, drive)) {
        	return 'U';
        }
    	
    	ArrayList<Coord> openSet = new ArrayList<Coord>();
        ArrayList<Coord> closedSet = new ArrayList<Coord>();
        openSet.add(start);
        Coord[][] cameFrom = new Coord[this.getWidth()][this.getHeight()];
        int[][] gScore = new int[this.getWidth()][this.getHeight()];
        int[][] fScore = new int[this.getWidth()][this.getHeight()];
        for(int i = 0; i < this.getWidth(); i++) {
            for(int j = 0; j < this.getHeight(); j++) {
                gScore[i][j] = 1000000;
                fScore[i][j] = 1000000; //1 million is effectively infinity
            }
        }
        fScore[start.xpos][start.ypos] = Math.abs(start.xpos-dest.xpos)+Math.abs(start.ypos-dest.ypos);
        
        while(!openSet.isEmpty()) {
            Coord current = null;
            for(int i = 0; i < openSet.size(); i++) {
                if(current == null || fScore[openSet.get(i).xpos][openSet.get(i).ypos] < fScore[current.xpos][current.ypos]) {
                    current = openSet.get(i);
                }
            }
            if(current.equals(dest)) {
                Coord prev = cameFrom[current.xpos][current.ypos];
                while(!start.equals(prev)) {
                    current = prev;
                    prev = cameFrom[prev.xpos][prev.ypos];
                }
                if(current.ypos < start.ypos) {
                    return 'N';
                } else if(current.xpos > start.xpos) {
                    return 'E';
                } else if(current.ypos > start.ypos) {
                    return 'S';
                } else {
                    return 'W';
                }
            }
            openSet.remove(current);
            closedSet.add(current);

            List<Coord> neighbors = getNeighbors(current);
            for(int i  = 0; i < neighbors.size(); i++) {
                if(!closedSet.contains(neighbors.get(i))) {
                    int tentativegScore = gScore[current.xpos][current.ypos];
                    if(blocked(neighbors.get(i), drive)) {
                        tentativegScore += 10000;
                        if(neighbors.get(i).equals(dest)){
                        	System.out.println("target is a obstacle");
                        	return 'U';
                        }
                    } else {
                        tentativegScore += 1;
                    }
                    if(!openSet.contains(neighbors.get(i))) {
                        openSet.add(neighbors.get(i));
                        cameFrom[neighbors.get(i).xpos][neighbors.get(i).ypos] = current;
                        gScore[neighbors.get(i).xpos][neighbors.get(i).ypos] = tentativegScore;
                        fScore[neighbors.get(i).xpos][neighbors.get(i).ypos] = tentativegScore + Math.abs(neighbors.get(i).xpos-dest.xpos)+Math.abs(neighbors.get(i).ypos-dest.ypos);
                    }
                }
            }
        }
        return 'U'; //destination is unreachable
    }

    //Get neighbors for each coord, must be within bounds, and less than 10000 or we'll step forever
    public List<Coord> getNeighbors(Coord current){
        List<Coord> neighbors  = new ArrayList<Coord>();
        if(current.ypos - 1 >= 0)
            neighbors.add(new Coord(current.xpos, current.ypos-1));

        //need to find a way to check neighbors only within bounds for now...let it keep going
        neighbors.add(new Coord(current.xpos+1, current.ypos));
        neighbors.add(new Coord(current.xpos, current.ypos+1));

        if(current.xpos - 1 >= 0)
            neighbors.add(new Coord(current.xpos-1, current.ypos));
        
        return neighbors;
    }

    public boolean blocked(Coord pos, RoverDriveType drive) {
    	//System.out.println("checcking: " + pos.toString());
        Terrain ter = this.getTile(pos).getTerrain();
		if(this.getTile(pos).getHasRover()) {
			return true;
		}
        if(ter == Terrain.NONE) {
            return true;
        }
        if(ter == Terrain.SAND && drive != RoverDriveType.TREADS) {
            return true;
        }
        if(ter == Terrain.ROCK && drive != RoverDriveType.WALKER) {
            return true;
        }
        return false;
    }
    
    public Communication getCom() {
		return com;
	}

    //This method takes in globalMap from rover (hashMap) and updates corresponding tiles on this map.
    public void updatePlanet(Map<Coord, MapTile> map){
        //iterate through each object in hashMap
        for(Map.Entry<Coord, MapTile> entry : map.entrySet()){
            //Set map tile using setter in PlanetMap
        	MapTile tile = entry.getValue();
        	Coord coord = entry.getKey();
            if(globalMap.get(coord) == null){
            	if(coord.xpos >= 0 && coord.ypos >= 0){
            		//System.out.println("Setting tile in " + entry.getKey().toString() +" tile: " + tile.getTerrain().getTerString());
            		this.setTile(tile, coord.xpos, coord.ypos);
            		globalMap.put(coord, tile);
            	}
            }
        }
    }

}