package testUtillities;

import java.net.ServerSocket;
import java.util.HashMap;

import common.Coord;

import enums.RoverConfiguration;

public class TestHashMapValueCheck {
	public static void main(String[] args) throws Exception {
        System.out.println("test is running");
       
  
	
        HashMap<RoverConfiguration, Coord> roverHash = new HashMap<RoverConfiguration, Coord>();
        
        roverHash.put(RoverConfiguration.ROVER_00, new Coord(2,2));
        
        Coord coord2_2 = new Coord(2,2);
        Coord coord3_3 = new Coord(3,3);
        
        if(roverHash.containsValue(coord2_2)){
        	System.out.println("contains the value 2,2");
        } else {
        	System.out.println("no value 2,2");
        }
        
        if(roverHash.containsValue(coord3_3)){
        	System.out.println("contains the value 3,3");
        } else {
        	System.out.println("no value 3,3");
        }
        
        roverHash.put(RoverConfiguration.ROVER_00, new Coord(3,3));
        System.out.println("moved rover to 3,3");
        
        if(roverHash.containsValue(coord2_2)){
        	System.out.println("contains the value 2,2");
        } else {
        	System.out.println("no value 2,2");
        }
        
        if(roverHash.containsValue(coord3_3)){
        	System.out.println("contains the value 3,3");
        } else {
        	System.out.println("no value 3,3");
        }
	
	}

}
