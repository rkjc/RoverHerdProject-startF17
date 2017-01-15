package testUtillities;

import UI_support.FXmapViewerWindow;
import common.RoverLocations;

public class StartFXmapViewerWindow {

	public static void main(String[] args) {

		RoverLocations roverLocations = new RoverLocations();
		roverLocations.loadSmallExampleTestRoverLocations();

		FXmapViewerWindow viewWin = new FXmapViewerWindow(roverLocations);
		Thread thread = new Thread(viewWin);
		thread.start();
	}

}
