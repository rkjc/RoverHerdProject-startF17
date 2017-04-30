package testUtillities;

import java.util.ArrayList;

import controlServer.RoverStats;
import enums.RoverConfiguration;

public class TestBuildaRover {
	public static void main(String[] args) {
		System.out.println("test builda running");
		RoverStats bob = new RoverStats(RoverConfiguration.ROVER_00);
		
		System.out.println("rover name " + bob.getRoverName());
		System.out.println("rover movetime " + bob.getRoverLastMoveTime());
		System.out.println("rover drivetype " + bob.getRoverDrive());
		System.out.println("rover stuff " + bob.getRoverName().getMembers());
		
		ArrayList roverTools = (ArrayList) bob.getRoverName().getMembers();
		System.out.println("rover arraylist " + roverTools.get(0));
		
		String toool = bob.getRoverName().getMembers().get(1);
		System.out.println("direct stuff " + toool);
		
		System.out.println("test builda ending");
	}
}
