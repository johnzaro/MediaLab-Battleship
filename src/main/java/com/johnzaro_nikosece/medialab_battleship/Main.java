package com.johnzaro_nikosece.medialab_battleship;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main extends Application
{
	final String CARRIER_COLOR = "#f1c40f";
	final String BATTLESHIP_COLOR = "#34495e";
	final String CRUISER_COLOR = "#9b59b6";
	final String SUBMARINE_COLOR = "#2ecc71";
	final String DESTROYER_COLOR = "#95a5a6";
	
	Stage stage;
	
	List<Ship> playerShips;
	List<Ship> enemyShips;
	
	Label[][] playerCells;
	Label[][] enemyCells;

	public static void main(String[] args)
	{
		launch(args);
	}

	public void start(Stage primaryStage)
	{
		stage = primaryStage;
		stage.setTitle("MediaLab Battleship");
		stage.setResizable(false);
		
		Menu applicationMenu = new Menu("Application");
		MenuItem startMenu = new MenuItem("Start");
		MenuItem loadMenu = new MenuItem("Load");
		MenuItem exitMenu = new MenuItem("Exit");
		applicationMenu.getItems().addAll(startMenu, loadMenu, exitMenu);
		
		Menu detailsMenu = new Menu("Details");
		MenuItem enemyShipsMenu = new MenuItem("Enemy Ships");
		MenuItem playerShotsMenu = new MenuItem("Player Shots");
		MenuItem enemyShotsMenu = new MenuItem("Enemy Shots");
		detailsMenu.getItems().addAll(enemyShipsMenu, playerShotsMenu, enemyShotsMenu);
		
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(applicationMenu, detailsMenu);
		
		HBox topInfoHBox = new HBox();
		topInfoHBox.setPrefHeight(80);
		topInfoHBox.setAlignment(Pos.CENTER);
		topInfoHBox.setSpacing(30);
		
		Label numOfActiveShips = new Label("active ships");
		numOfActiveShips.setStyle("-fx-font-size: 25;");
		
		Label totalPoints = new Label("total points");
		totalPoints.setStyle("-fx-font-size: 25;");
		
		Label successfulShots = new Label("successful shots");
		successfulShots.setStyle("-fx-font-size: 25;");
		
		topInfoHBox.getChildren().addAll(numOfActiveShips, totalPoints, successfulShots);
		
		HBox hBoxFor2Grids = new HBox();
		hBoxFor2Grids.setAlignment(Pos.CENTER);
		hBoxFor2Grids.setFillHeight(true);
		hBoxFor2Grids.setSpacing(30);
		
		GridPane playersGridPane = new GridPane();
		playersGridPane.setGridLinesVisible(true);
		playersGridPane.setPrefSize(900, 900);
		playersGridPane.setAlignment(Pos.CENTER);
		
		GridPane enemyGridPane = new GridPane();
		enemyGridPane.setGridLinesVisible(true);
		enemyGridPane.setPrefSize(900, 900);
		enemyGridPane.setAlignment(Pos.CENTER);
		
		for(int i = 0; i < 11; i++) // set row && column sizes
		{
			ColumnConstraints c = new ColumnConstraints();
			c.setPercentWidth(i == 0 ? 3 : 10);
			playersGridPane.getColumnConstraints().add(c);
			enemyGridPane.getColumnConstraints().add(c);
			
			RowConstraints r = new RowConstraints();
			r.setPercentHeight(i == 0 ? 3 : 10);
			playersGridPane.getRowConstraints().add(r);
			enemyGridPane.getRowConstraints().add(r);
		}
		
		for(int i = 0; i < 10; i++) // add horizontal && vertical indexes
		{
			Label l = new Label(String.format("%d", i));
			l.setStyle("-fx-font-size: 15;");
			GridPane.setHalignment(l, HPos.CENTER);
			playersGridPane.add(l, i + 1, 0);
			
			l = new Label(String.format("%d", i));
			l.setStyle("-fx-font-size: 15;");
			GridPane.setHalignment(l, HPos.CENTER);
			playersGridPane.add(l, 0, i + 1);
			
			l = new Label(String.format("%d", i));
			l.setStyle("-fx-font-size: 15;");
			GridPane.setHalignment(l, HPos.CENTER);
			enemyGridPane.add(l, i + 1, 0);
			
			l = new Label(String.format("%d", i));
			l.setStyle("-fx-font-size: 15;");
			GridPane.setHalignment(l, HPos.CENTER);
			enemyGridPane.add(l, 0, i + 1);
		}
		
		playerCells = new Label[10][10];
		enemyCells = new Label[10][10];
		
		for(int i = 0; i < 10; i++) // add labels to grids
		{
			for(int j = 0; j < 10; j++)
			{
				playerCells[i][j] = new Label();
				playerCells[i][j].setPrefSize(86, 86);
				GridPane.setHalignment(playerCells[i][j], HPos.CENTER);
				playersGridPane.add(playerCells[i][j], i + 1, j + 1);
				
				enemyCells[i][j] = new Label();
				enemyCells[i][j].setPrefSize(86, 86);
				GridPane.setHalignment(enemyCells[i][j], HPos.CENTER);
				enemyGridPane.add(enemyCells[i][j], i + 1, j + 1);
			}
		}
		
		makeMapBlue();
		
		hBoxFor2Grids.getChildren().addAll(playersGridPane, enemyGridPane);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topInfoHBox);
		borderPane.setCenter(hBoxFor2Grids);
		
		VBox sceneContainerVBox = new VBox();
		sceneContainerVBox.getChildren().addAll(menuBar, borderPane);
		
		Scene scene = new Scene(sceneContainerVBox, 1920, 1080);
		stage.setScene(scene);
		stage.show();
		
		loadMenu.setOnAction((ActionEvent t) ->
		{
			int id = ScenarioId.display("MediaLab Battleship", "Provide a SCENARIO-ID");
			
			readInputFiles(id);
		});
		
		exitMenu.setOnAction((ActionEvent t) ->
		{
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("MediaLab Battleship");
			alert.setHeaderText("Do you really want to exit?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK)
				System.exit(0);
		});
	}
	
	void makeMapBlue()
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				playerCells[i][j].setStyle("-fx-background-color: #3498db;");
				enemyCells[i][j].setStyle("-fx-background-color: #3498db;");
			}
		}
	}
	
	void readInputFiles(int id)
	{
		makeMapBlue();
		
		playerShips = new ArrayList<>();
		enemyShips = new ArrayList<>();
		
		readInputFile(true, id);
		readInputFile(false, id);
		
		String color;
		
		for(Ship s: playerShips)
		{
			color = switch(s.typeOfShip)
			{
				case 1 -> CARRIER_COLOR;
				case 2 -> BATTLESHIP_COLOR;
				case 3 -> CRUISER_COLOR;
				case 4 -> SUBMARINE_COLOR;
				case 5 -> DESTROYER_COLOR;
				default -> "";
			};
			
			if(s.position.isHorizontal())
			{
				for(int i = s.position.getY(); i < s.position.getY() + s.shipSize; i++)
					playerCells[i][s.position.getX()].setStyle("-fx-background-color: " + color + ";");
			}
			else
			{
				for(int i = s.position.getX(); i < s.position.getX() + s.shipSize; i++)
					playerCells[s.position.getY()][i].setStyle("-fx-background-color: " + color + ";");
			}
		}
	}
	
	void readInputFile(boolean isPlayersFile, int id)
	{
		try
		{
			List<Ship> currentList = isPlayersFile ? playerShips : enemyShips;
			
			String fileName = isPlayersFile ? String.format("player_%d.txt", id) : String.format("enemy_%d.txt", id);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Main.class.getResource("/medialab/" + fileName).getFile())));
			
			String line;
			while((line = reader.readLine()) != null)
			{
				String[] elements = line.split(",");
				
				if(elements.length == 4)
				{
					int typeOfShip = Integer.parseInt(elements[0]);
					int x = Integer.parseInt(elements[1]);
					int y = Integer.parseInt(elements[2]);
					boolean isHorizontal = Integer.parseInt(elements[3]) == 1; // horizontal == 1, vertical == 2
					
					for(Ship s: currentList)
					{
						if(s.typeOfShip == typeOfShip)
							throw new InvalidCountException();
					}
					
					Ship ship = switch(typeOfShip)
					{
						case 1 -> new Carrier(x, y, isHorizontal);
						case 2 -> new Battleship(x, y, isHorizontal);
						case 3 -> new Cruiser(x, y, isHorizontal);
						case 4 -> new Submarine(x, y, isHorizontal);
						case 5 -> new Destroyer(x, y, isHorizontal);
						default -> null;
					};
					
					if(ship != null)
					{
						if(!isHorizontal)
						{
							if(x < 0 || x + ship.shipSize - 1 > 9 || y < 0 || y > 9)
								throw new OversizeException();
						}
						else
						{
							if(x < 0 || x > 9 || y < 0 || y + ship.shipSize - 1 > 9)
								throw new OversizeException();
						}
						
						
						
						currentList.add(ship);
					}
					else
						throw new WrongFileFormatException();
				}
				else
					throw new WrongFileFormatException();
			}
			reader.close();
		}
		catch(IOException e)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("FileNotFoundException");
			alert.setContentText("Δεν βρέθηκε το αρχείο εισόδου.");
			alert.showAndWait();
		}
		catch(NumberFormatException | WrongFileFormatException e)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("WrongFileFormatException");
			alert.setContentText("Υπάρχει σφάλμα στα αρχεία εισόδου.");
			alert.showAndWait();
		}
		catch(InvalidCountException e)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("InvalidCountException");
			alert.setContentText("Δεν μπορούν να υπάρχουν περισσότερα από ένα πλοία για κάθε τύπο.");
			alert.showAndWait();
		}
		catch(OversizeException e)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("OversizeException");
			alert.setContentText("Ένα πλοίο δεν μπορεί να βγαίνει εκτός των ορίων του ταμπλό.");
			alert.showAndWait();
		}
	}
}






