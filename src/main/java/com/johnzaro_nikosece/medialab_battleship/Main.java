package com.johnzaro_nikosece.medialab_battleship;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

public class Main extends Application
{
	Stage stage;
	
	TextField textField;
	
	User player, cpu;
	
	List<Ship> playerShips;
	List<Ship> enemyShips;
	
	CustomGridCell[][] playerCells;
	CustomGridCell[][] enemyCells;
	
	boolean mapIsLoaded;

	public static void main(String[] args)
	{
		launch(args);
	}

	public void start(Stage primaryStage)
	{
		stage = primaryStage;
		stage.setTitle("MediaLab Battleship");
		stage.setResizable(false);
		
		mapIsLoaded = false;
		
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
		menuBar.setCursor(Cursor.HAND);
		menuBar.setStyle("-fx-font-size: 15;");
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
		
		Label playerLabel = new Label("Player");
		playerLabel.setStyle("-fx-font-size: 25;");
		playerLabel.setPrefWidth(900);
		playerLabel.setAlignment(Pos.CENTER);
		
		Label cpuLabel = new Label("CPU");
		cpuLabel.setStyle("-fx-font-size: 25;");
		cpuLabel.setPrefWidth(900);
		cpuLabel.setAlignment(Pos.CENTER);
		
		HBox usersHBox = new HBox();
		usersHBox.setAlignment(Pos.CENTER);
		usersHBox.setFillHeight(true);
		usersHBox.setPrefHeight(50);
		usersHBox.setSpacing(30);
		usersHBox.getChildren().addAll(playerLabel, cpuLabel);
		
		VBox topVBox = new VBox();
		topVBox.getChildren().addAll(topInfoHBox, usersHBox);
		
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
		
		playerCells = new CustomGridCell[10][10];
		enemyCells = new CustomGridCell[10][10];
		
		for(int xi = 0; xi < 10; xi++) // add labels to grids
		{
			for(int yi = 0; yi < 10; yi++)
			{
				playerCells[xi][yi] = new CustomGridCell(true);
				GridPane.setHalignment(playerCells[xi][yi], HPos.CENTER);
				playersGridPane.add(playerCells[xi][yi], yi + 1, xi + 1);
				
				enemyCells[xi][yi] = new CustomGridCell(false);
				GridPane.setHalignment(enemyCells[xi][yi], HPos.CENTER);
				enemyGridPane.add(enemyCells[xi][yi], yi + 1, xi + 1);
				
				int finalXi = xi;
				int finalYi = yi;
				enemyCells[xi][yi].setOnMouseClicked(e ->
				{
					textField.setText("(" + finalXi + "," + finalYi + ")");
				});
			}
		}
		
		hBoxFor2Grids.getChildren().addAll(playersGridPane, enemyGridPane);
		
		Label shootLabel = new Label("Target Cell:");
		
		textField = new TextField();
		textField.setPrefSize(120, 35);
		textField.setEditable(false);
		
		Button shootButton = new Button("Shoot");
		shootButton.setPrefSize(120, 35);
		shootButton.setCursor(Cursor.HAND);
		
		HBox bottomHBox = new HBox();
		bottomHBox.setFillHeight(true);
		bottomHBox.setAlignment(Pos.CENTER);
		bottomHBox.setSpacing(30);
		bottomHBox.setPrefHeight(70);
		bottomHBox.getChildren().addAll(shootLabel, textField, shootButton);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topVBox);
		borderPane.setCenter(hBoxFor2Grids);
		borderPane.setBottom(bottomHBox);
		
		VBox sceneContainerVBox = new VBox();
		sceneContainerVBox.getChildren().addAll(menuBar, borderPane);
		
		Scene scene = new Scene(sceneContainerVBox, 1920, 1080);
		stage.setScene(scene);
		stage.show();
		
		loadMenu.setOnAction((ActionEvent t) ->
		{
			int id = ScenarioId.display("MediaLab Battleship", "Provide a SCENARIO-ID");
			
			resetGrid();
			
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
		
		startMenu.setOnAction(e ->
		{
			if(mapIsLoaded)
				startGame();
			else
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("MediaLab Battleship");
				alert.setHeaderText("Game Is Not Loaded Yet, Please Load A Game To Start.");
				alert.showAndWait();
			}
		});
	}
	
	void resetGrid()
	{
		for(int xi = 0; xi < 10; xi++) // add labels to grids
		{
			for(int yi = 0; yi < 10; yi++)
			{
				playerCells[xi][yi].resetCell();
				enemyCells[xi][yi].resetCell();
			}
		}
	}
	
	void startGame()
	{
		player = new User();
		cpu = new User();
		
		Random random = new Random();
		boolean playerStartsFirst = random.nextBoolean();
		
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("MediaLab Battleship");
		alert.setHeaderText(playerStartsFirst ? "You Start First!" : "CPU Starts First!");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK)
		{
			if(!playerStartsFirst)
			{
				Task<Void> sleeper = new Task<>()
				{
					protected Void call()
					{
						try { Thread.sleep(3000); } catch (InterruptedException ignored) { }
						return null;
					}
				};
				
				sleeper.setOnSucceeded(event ->
				{
					// cpu turn
				});
				new Thread(sleeper).start();
			}
		}
	}
	
	void readInputFiles(int id)
	{
		playerShips = new ArrayList<>();
		enemyShips = new ArrayList<>();
		
		boolean playerFileOK = readInputFile(true, id);
		boolean enemyFileOK = readInputFile(false, id);
		
		mapIsLoaded = playerFileOK && enemyFileOK;
	}
	
	boolean readInputFile(boolean isPlayersFile, int id)
	{
		try
		{
			List<Ship> currentShipList = isPlayersFile ? playerShips : enemyShips;
			CustomGridCell[][] currentGridList = isPlayersFile ? playerCells : enemyCells;
			
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
					
					for(Ship s: currentShipList)
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
						if(isHorizontal)
						{
							if(x < 0 || x > 9 || y < 0 || y + ship.shipSize - 1 > 9)
								throw new OversizeException();
						}
						else
						{
							if(x < 0 || x + ship.shipSize - 1 > 9 || y < 0 || y > 9)
								throw new OversizeException();
						}
						
						if(ship.position.isHorizontal())
						{
							for(int xi = Math.max(0, ship.position.getX() - 1); xi < Math.min(currentGridList.length - 1, ship.position.getX() + 1); xi++) // row
							{
								for(int yi = Math.max(0, ship.position.getY() - 1); yi < Math.min(currentGridList[yi].length - 1, ship.position.getY() + ship.shipSize + 1); yi++) // column
								{
									if(!currentGridList[xi][yi].isEmptyCell)
									{
										if(xi == ship.position.getX() && yi >= ship.position.getY() && yi < ship.position.getY() + ship.shipSize)
											throw new OverlapTilesException();
										else
											throw new AdjacentTilesException();
									}
								}
							}
							
							for(int yi = ship.position.getY(); yi < ship.position.getY() + ship.shipSize; yi++)
								currentGridList[ship.position.getX()][yi].setShip(ship);
						}
						else
						{
							for(int xi = Math.max(0, ship.position.getX() - 1); xi < Math.min(currentGridList.length - 1, ship.position.getX() + ship.shipSize + 1); xi++) // row
							{
								for(int yi = Math.max(0, ship.position.getY() - 1); yi < Math.min(currentGridList[yi].length - 1, ship.position.getY() + 1); yi++) // column
								{
									if(!currentGridList[xi][yi].isEmptyCell)
									{
										if(xi >= ship.position.getX() && xi < ship.position.getX() + ship.shipSize && yi == ship.position.getY())
											throw new OverlapTilesException();
										else
											throw new AdjacentTilesException();
									}
								}
							}
							
							for(int xi = ship.position.getX(); xi < ship.position.getX() + ship.shipSize; xi++)
								currentGridList[xi][ship.position.getY()].setShip(ship);
						}
						
						currentShipList.add(ship);
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
			resetGrid();
			
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("FileNotFoundException");
			alert.setContentText("Δεν βρέθηκε το αρχείο εισόδου.");
			alert.showAndWait();
			
			return false;
		}
		catch(NumberFormatException | WrongFileFormatException e)
		{
			resetGrid();
			
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("WrongFileFormatException");
			alert.setContentText("Υπάρχει σφάλμα στα αρχεία εισόδου.");
			alert.showAndWait();
			
			return false;
		}
		catch(InvalidCountException e)
		{
			resetGrid();
			
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("InvalidCountException");
			alert.setContentText("Δεν μπορούν να υπάρχουν περισσότερα από ένα πλοία για κάθε τύπο.");
			alert.showAndWait();
			
			return false;
		}
		catch(OversizeException e)
		{
			resetGrid();
			
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("OversizeException");
			alert.setContentText("Ένα πλοίο δεν μπορεί να βγαίνει εκτός των ορίων του ταμπλό.");
			alert.showAndWait();
			
			return false;
		}
		catch(OverlapTilesException e)
		{
			resetGrid();
			
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("OverlapTilesException");
			alert.setContentText("Ένα πλοίο δεν μπορεί να τοποθετηθεί σε κελί που ήδη έχει άλλο πλοίο.");
			alert.showAndWait();
			
			return false;
		}
		catch(AdjacentTilesException e)
		{
			resetGrid();
			
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("AdjacentTilesException");
			alert.setContentText("Ένα πλοίο δεν μπορεί να εφάπτεται κάθετα ή οριζόντια με κανένα άλλο πλοίο, έστω και για ένα κελί.");
			alert.showAndWait();
			
			return false;
		}
		
		return true;
	}
}






