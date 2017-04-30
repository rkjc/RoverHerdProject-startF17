package testUtillities;

import controlServer.RoverStats;
import enums.RoverConfiguration;

public class RoverNameTest {
	public static void main(String[] args) {
		System.out.println("test roverName test running");
		
		String name = "ROVER_00";
				
        RoverConfiguration rname = RoverConfiguration.getEnum(name); 
        
        System.out.println("SWARM: make a rover name " + rname);
        
        //Rover rover = new Rover(rname);
        
        
        
	}
}
