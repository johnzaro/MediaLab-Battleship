package com.johnzaro_nikosece.medialab_battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("MediaLab Battleship");
		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root, 1536, 864));
		primaryStage.show();
	}
}
