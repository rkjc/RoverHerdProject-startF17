package UI_support;

import common.RoverLocations;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class FXmapViewerWindow extends Application implements Runnable {

	RoverLocations roverLocations;

	public FXmapViewerWindow() {
		// this.roverLocations = roverLoc;
	}

	public FXmapViewerWindow(RoverLocations roverLoc) {
		// this.roverLocations = roverLoc;
		System.out.println("RoverLocations constructor");
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// BorderPane root = new BorderPane();
			StackPane root = new StackPane();
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			Button btn = new Button();
			btn.setText("Say 'Hello World'");
			btn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					System.out.println("Hello World!");
				}
			});

			root.getChildren().add(btn);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		launch();
	}

}
