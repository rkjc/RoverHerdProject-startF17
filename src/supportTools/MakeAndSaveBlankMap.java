package supportTools;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import MapSupport.Coord;
import MapSupport.MapTile;
import MapSupport.PlanetMap;
import MapSupport.RoverLocations;
import MapSupport.ScanMap;
import MapSupport.ScienceLocations;
import controlServer_RCP.SwarmMapInit;
import enums.Science;
import enums.Terrain;
import json.MyWriter;

public class MakeAndSaveBlankMap {

	public static void main(String[] args) throws IOException {
		int mapWidth = 50;
		int mapHeight = 30;
		
		String fileName = "Map" + mapWidth + "x" + mapHeight + "blank.txt";
			
		SwarmMapInit mapInit = new SwarmMapInit(fileName, mapWidth, mapHeight, new PlanetMap(mapWidth, mapHeight), new RoverLocations(), new ScienceLocations());

		mapInit.saveToDisplayTextFile(fileName);
		mapInit.printToDisplayTextFile();
				
	}
}
