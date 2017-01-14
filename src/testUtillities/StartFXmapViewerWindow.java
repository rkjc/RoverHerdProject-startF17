package testUtillities;

import UI_support.FXmapViewerWindow;
import common.RoverLocations;

public class StartFXmapViewerWindow {

	public static void main(String[] args) {

		RoverLocations roverLocations = new RoverLocations();
		roverLocations.loadSmallExampleTestRoverLocations();

		Thread thread = new Thread(new FXmapViewerWindow(roverLocations));
		thread.start();
	}

}
