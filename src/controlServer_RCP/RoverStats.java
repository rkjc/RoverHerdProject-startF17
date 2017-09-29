package controlServer_RCP;

import java.util.ArrayList;

import enums.RoverDriveType;
import enums.RoverConfiguration;
import enums.RoverToolType;
import enums.Science;

// This rover object is used by the RoverCommandProcessor to keep track of the stats for a particular bot
// It is not directly accessible to the RoverXX code

public class RoverStats {
	private RoverDriveType driveType;
	private RoverConfiguration roverName;
	private RoverToolType tool_1;
	private RoverToolType tool_2;
	private long lastMoveTime;
	private long lastGatherTime;
	private long lastRequestTime;
	private int requestCount;
	private long powerReserve; //Battery charge - not currently used
	private long maxPowerCap;  //Battery capacity - not currently used
	private long powerRegenRate; //RTG output - not currently used
	private long driveEfficiency; //wear and tear factor on the drive system - not currently used
	
	// public for the arrayList just because it is easier to deal with than getters/setters
    public ArrayList<Science> scienceCargo;
	
	
	public RoverStats(RoverConfiguration rname){
		this.roverName = rname;
		System.out.println("ROVER: building a rover " + rname);
		// Rover type equipment is stored in the RoverName enum
		
		// use the RoverName to get the drive type string, use the string to get the enum type
		String drivetype = rname.getMembers().get(0);
		System.out.println("ROVER: drivetype " + drivetype);
		RoverDriveType dtype = RoverDriveType.getEnum(drivetype);
		
		// use the drive type enum to set this rover object drive type
		this.driveType = dtype;
		System.out.println("ROVER: this.drivetype " + dtype);
		
		//tool 1 and 2 uses the same procedure as setting the drivetype
		String tt1 = rname.getMembers().get(1);
		System.out.println("ROVER: tt1 string is " + tt1);
		RoverToolType ttype1 = RoverToolType.getEnum(tt1);
		this.tool_1 = ttype1;
		System.out.println("ROVER: this.tool_1 " + ttype1);
		
        // Make an arrayList to hold any collected Science
        scienceCargo = new ArrayList<Science>();
		
		RoverToolType ttype2 = RoverToolType.getEnum(rname.getMembers().get(2));
		this.tool_2 = ttype2;
		System.out.println("ROVER: this.tool_2 " + ttype2);
		
		requestCount = 0;
		
		//stores the current timestamp
		this.lastMoveTime = System.currentTimeMillis();
		this.lastRequestTime = System.currentTimeMillis();
	}
	
	public RoverConfiguration getRoverName(){
		return this.roverName;
	}
	
	public RoverDriveType getRoverDrive(){
		return this.driveType;
	}
	
	public RoverToolType getTool_1(){
		return this.tool_1;		
	}
	
	public RoverToolType getTool_2(){
		return this.tool_2;		
	}
	
	public long getRoverLastGatherTime(){
		return this.lastGatherTime;
	}
	public void updateGatherTime(){
		this.lastGatherTime = System.currentTimeMillis();
	}
	
	public long getRoverLastMoveTime(){
		return this.lastMoveTime;
	}
	public void updateMoveTime(){
		this.lastMoveTime = System.currentTimeMillis();
	}
	
	public long getRoverRequestCount(){
		if((System.currentTimeMillis() - lastRequestTime) < 1000){
			++requestCount;
		} else {
			lastRequestTime = System.currentTimeMillis();
			requestCount = 0;
		}	
		return this.requestCount;
	}

}
