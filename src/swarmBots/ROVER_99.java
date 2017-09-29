package swarmBots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import MapSupport.Coord;
import MapSupport.MapTile;
import MapSupport.ScanMap;
import common.Rover;
import enums.Terrain;



/**
 * The seed that this program is built on is a chat program example found here:
 * publishing their code examples
 * * http://cs.lmu.edu/~ray/notes/javanetexamples/ Many thanks to the authors for
 */

public class ROVER_99 extends Rover {

	BufferedReader in;
	PrintWriter out;
	String rovername;
	ScanMap scanMap;
	int sleepTime;
	String SERVER_ADDRESS = "localhost";
	static final int PORT_ADDRESS = 9537;

	public ROVER_99() {
		// constructor
		System.out.println("ROVER_99 rover object constructed");
		rovername = "ROVER_99";
		SERVER_ADDRESS = "localhost";
		// this should be a safe but slow timer value
		sleepTime = 300; // in milliseconds - smaller is faster, but the server will cut connection if it is too small
	}
	
	public ROVER_99(String serverAddress) {
		// constructor
		System.out.println("ROVER_99 rover object constructed");
		rovername = "ROVER_99";
		SERVER_ADDRESS = serverAddress;
		sleepTime = 200; // in milliseconds - smaller is faster, but the server will cut connection if it is too small
	}

	/**
	 * Connects to the server then enters the processing loop.
	 */
	private void run() throws IOException, InterruptedException {

		// Make connection and initialize streams
		//TODO - need to close this socket
		Socket socket = new Socket(SERVER_ADDRESS, PORT_ADDRESS); // set port here
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		//Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// Process all messages from server, wait until server requests Rover ID
		// name
		while (true) {
			String line = in.readLine();
			if (line.startsWith("SUBMITNAME")) {
				out.println(rovername); // This sets the name of this instance
										// of a swarmBot for identifying the
										// thread to the server
				break;
			}
		}

		// ******** Rover logic *********
		// int cnt=0;
		String line = "";

		int counter = 0;
		
		boolean goingSouth = false;
		boolean goingEast = false;
		
		boolean stuck = false; // just means it did not change locations between requests,
		int stillStuck = 0;						// could be velocity limit or obstruction etc.
		
		boolean blocked = false;

		String[] cardinals = new String[4];
		cardinals[0] = "N";
		cardinals[1] = "E";
		cardinals[2] = "S";
		cardinals[3] = "W";

		String currentDir = cardinals[0];
		Coord currentLoc = null;
		Coord previousLoc = null;
		

		// start Rover controller process
		while (true) {

			// currently the requirements allow sensor calls to be made with no
			// simulated resource cost
			
			
			// **** location call ****
			out.println("LOC");
			line = in.readLine();
            if (line == null) {
            	System.out.println("ROVER_99 check connection to server");
            	line = "";
            }
			if (line.startsWith("LOC")) {
				// loc = line.substring(4);
				currentLoc = extractLocationFromString(line);
			}
			System.out.println("ROVER_99 currentLoc at start: " + currentLoc);
			
			// after getting location set previous equal current to be able to check for stuckness and blocked later
			previousLoc = currentLoc;
			
			
			
			// **** get equipment listing ****			
			ArrayList<String> equipment = new ArrayList<String>();
			equipment = getEquipment();
			//System.out.println("ROVER_99 equipment list results drive " + equipment.get(0));
			System.out.println("ROVER_99 equipment list results " + equipment + "\n");
			
	

			// ***** do a SCAN *****
			//System.out.println("ROVER_99 sending SCAN request");
			this.doScan();
			scanMap.debugPrintMap();
			
			
			

			
			// MOVING
			
			if(stuck == true){
				stillStuck++;
			} else {
				stillStuck = 0;
			}

			// try moving east 5 block if blocked
			if (blocked || stillStuck == 10) {
				for (int i = 0; i < 5; i++) {
					out.println("MOVE S");
					//System.out.println("ROVER_00 request move E");
					Thread.sleep(1100);
				}
				blocked = false;
					//reverses direction after being blocked
					goingEast = !goingEast;
				
			} else {


				// pull the MapTile array out of the ScanMap object
				MapTile[][] scanMapTiles = scanMap.getScanMap();
				int centerIndex = (scanMap.getEdgeSize() - 1)/2;
				// tile S = y + 1; N = y - 1; E = x + 1; W = x - 1

				if (goingEast) {
					// check scanMap to see if path is blocked to the south
					// (scanMap may be old data by now)
					if (scanMapTiles[centerIndex  +1][centerIndex].getHasRover() 
							//|| scanMapTiles[centerIndex +1][centerIndex].getTerrain() == Terrain.ROCK
							|| scanMapTiles[centerIndex +1][centerIndex].getTerrain() == Terrain.NONE) {
						blocked = true;
					} else {
						// request to server to move
						out.println("MOVE E");
						System.out.println("ROVER_99 request move E");
					}
					
				} else {
					// check scanMap to see if path is blocked to the north
					// (scanMap may be old data by now)
					System.out.println("ROVER_99 scanMapTiles[2][1].getHasRover() " + scanMapTiles[2][1].getHasRover());
					System.out.println("ROVER_99 scanMapTiles[2][1].getTerrain() " + scanMapTiles[2][1].getTerrain().toString());
					
					if (scanMapTiles[centerIndex -1][centerIndex].getHasRover() 
							//|| scanMapTiles[centerIndex -1][centerIndex].getTerrain() == Terrain.ROCK
							|| scanMapTiles[centerIndex -1][centerIndex].getTerrain() == Terrain.NONE) {
						blocked = true;
					} else {
						// request to server to move
						out.println("MOVE W");
						System.out.println("ROVER_99 request move W");
					}
					
				}

			}

			// another call for current location
			out.println("LOC");
			line = in.readLine();
			if (line.startsWith("LOC")) {
				currentLoc = extractLocationFromString(line);
			}

			System.out.println("ROVER_99 currentLoc after recheck: " + currentLoc);
			System.out.println("ROVER_99 previousLoc: " + previousLoc);

			// test for stuckness
			stuck = currentLoc.equals(previousLoc);

			System.out.println("ROVER_99 stuck test " + stuck);
			System.out.println("ROVER_99 blocked test " + blocked);

			
			Thread.sleep(sleepTime);
			
			System.out.println("ROVER_99 ------------ bottom process control --------------"); 

		}

	}

	// ################ Support Methods ###########################
	

	


	
	

	/**
	 * Runs the client
	 */
	public static void main(String[] args) throws Exception {
		ROVER_99 client = new ROVER_99();
		client.run();
	}
}
