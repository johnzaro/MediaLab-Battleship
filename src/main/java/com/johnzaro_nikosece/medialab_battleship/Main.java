package com.johnzaro_nikosece.medialab_battleship;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/*public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

/*	public void start(Stage primaryStage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("MediaLab Battleship");
		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root, 1536, 864));
		primaryStage.show();
	}
 }*/


public class Main extends Application {  //application is for javafx
//	private  ReadFile Readfile = new ReadFile();
	Stage window;
	BorderPane layout;

	public static void main(String[] args) {

		launch(args);

	}

	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("MediaLab Battleship");
		//file menu
		Menu application = new Menu("Applicaton");
		Menu Details = new Menu("Details");
		//menu items
		// application.getItems().add(new MenuItem("Start"));
		//  application.getItems().add(new MenuItem("Load"));
		//  application.getItems().add(new MenuItem("Exit"));

		MenuItem item1 = new MenuItem("Start");
		MenuItem item2 = new MenuItem("Load");
		MenuItem item3 = new MenuItem("Exit");
		application.getItems().addAll(item1, item2, item3);
		Details.getItems().add(new MenuItem("Enemy Ships"));
		Details.getItems().add(new MenuItem("Player Shots"));
		Details.getItems().add(new MenuItem("Enemy Shots"));
		//main menu bar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(application, Details);

		layout = new BorderPane();
		layout.setTop(menuBar);
		Scene scene = new Scene(layout, 1920, 1080);
		window.setScene(scene);
		window.show();
		item2.setOnAction((ActionEvent t) -> {
			boolean result = ScenarioId.display("MediaLab Battleship", "Write your SenarioID");
			if (result == true) {


				window.close();

			}
		});
		item3.setOnAction((ActionEvent t) -> {
			boolean result = ComfirmBox.display("MediaLab Battleship", "Do you want to exit?");
			if (result == true) {
				window.close();

			}
		});
	}
}






