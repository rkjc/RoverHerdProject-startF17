package MapSupport;

import enums.RoverConfiguration;
import enums.Science;
import enums.Terrain;

public class MapTile {
	private Terrain terrain;
	public int elevation = 0;	// not currently used
	public int count = 0;  //undefined usage, possibly use on ScanMap for tracking visits
	private Science science;	//for use on ScanMap, not used on PlanetMap
	private boolean hasRover;	//for use on ScanMap, not used on PlanetMap
	private String scannedBy = null; //for keeping track of which rover first discovered this tile
	
	public MapTile(){
		this.terrain = Terrain.SOIL;
		this.science = Science.NONE;
		this.hasRover = false;
	}
	
	public MapTile(int notUsed){
		// use any integer as an argument to create MapTile with no terrain
		this.terrain = Terrain.NONE;
		this.science = Science.NONE;
		this.hasRover = false;
	}
	
	public MapTile(String terrainLetter){
		// use appropriate string to create MapTile with matching terrain
		this.terrain = Terrain.getEnum(terrainLetter);
		this.science = Science.NONE;
		this.hasRover = false;
	}
	
	public MapTile(Terrain ter, int elev){
		this.terrain = ter;
		this.science = Science.NONE;
		this.elevation = elev;
		this.hasRover = false;
	}
	
	public MapTile(Terrain ter, Science sci, boolean hasR){
		this.terrain = ter;
		this.science = sci;
		this.hasRover = hasR;
	}
	
	public MapTile(Terrain ter, Science sci, int elev, boolean hasR){
		this.terrain = ter;
		this.science = sci;
		this.elevation = elev;
		this.hasRover = hasR;
	}
	
	public MapTile(Terrain ter, Science sci, int elev, boolean hasR, String scanBy, int cnt){
		this.terrain = ter;
		this.science = sci;
		this.elevation = elev;
		this.hasRover = hasR;
		this.count = cnt;
		this.scannedBy = scanBy;
	}
	
	public MapTile getCopyOfMapTile(){
		MapTile rTile = new MapTile(this.terrain, this.science, this.elevation, this.hasRover, this.scannedBy, this.count);	
		return rTile;
	}

	// No setters in this class to make it thread safe
	
	public Terrain getTerrain() {
		return this.terrain;
	}

	public Science getScience() {
		return this.science;
	}

	public int getElevation() {
		return this.elevation;
	}
	
	public boolean getHasRover() {
		return this.hasRover;
	}
	
	public String getScannedBy() {
		return this.scannedBy;
	}
	
	// well, this might have broke the thread safe rule
	
	public void setHasRoverTrue(){
		this.hasRover = true;
	}
	
	public void setHasRoverFalse(){
		this.hasRover = false;
	}
	
	public void setScience(Science sci){
		this.science = sci;
	}
	
	public boolean setScannedBy(String roverName) {
		if(this.scannedBy == null){
			this.scannedBy = roverName;
			return true;
		} else {
			return false;
		}
	}
}
