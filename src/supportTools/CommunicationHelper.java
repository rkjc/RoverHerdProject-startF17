package supportTools;

import common.MapTile;

import org.json.simple.JSONObject;

import enums.Science;
import enums.Terrain;

/**
 * Created by samskim on 5/12/16.
 */
public class CommunicationHelper {

    public static MapTile convertToMapTile(JSONObject o){
        Terrain terrain = getTerrain((String) o.get("terrain"));
        Science science = getScience((String) o.get("science"));
        MapTile tile = new MapTile(terrain, science, 0, false);
        return tile;
    }

    public static Terrain getTerrain(String str) {
        Terrain output;

        switch (str) {
            case "NONE":
                output = Terrain.NONE;
                break;
            case "ROCK":
                output = Terrain.ROCK;
                break;
            case "SOIL":
                output = Terrain.SOIL;
                break;
            case "GRAVEL":
                output = Terrain.GRAVEL;
                break;
            case "SAND":
                output = Terrain.SAND;
                break;
            case "FLUID":
                output = Terrain.FLUID;
                break;

            default:
                output = Terrain.NONE;
        }
        return output;

    }

    public static Science getScience(String input){
        Science output;

        switch(input){
            case "NONE":
                output = Science.NONE;
                break;
            case "RADIOACTIVE":
                output = Science.RADIOACTIVE;
                break;
            case "ORGANIC":
                output = Science.ORGANIC;
                break;
            case "MINERAL":
                output = Science.MINERAL;
                break;
            case "ARTIFACT":
                output = Science.ARTIFACT;
                break;
            case "CRYSTAL":
                output = Science.CRYSTAL;
                break;

            default:
                output = Science.NONE;
        }
        return output;
    }


}
