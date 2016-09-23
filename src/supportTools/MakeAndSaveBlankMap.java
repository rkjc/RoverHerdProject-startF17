package supportTools;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import common.Coord;
import common.MapTile;
import common.PlanetMap;
import common.RoverLocations;
import common.ScanMap;
import common.ScienceLocations;
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
