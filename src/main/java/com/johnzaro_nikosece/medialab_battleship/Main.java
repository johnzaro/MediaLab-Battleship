package com.johnzaro_nikosece.medialab_battleship;

import com.johnzaro_nikosece.medialab_battleship.customControls.CustomGridCell;
import com.johnzaro_nikosece.medialab_battleship.customControls.IndexLabel;
import com.johnzaro_nikosece.medialab_battleship.dataStructures.User;
import com.johnzaro_nikosece.medialab_battleship.popups.EnemyShipsPopUp;
import com.johnzaro_nikosece.medialab_battleship.popups.LastShotsPopUp;
import com.johnzaro_nikosece.medialab_battleship.popups.LoadScenarioIDPopUp;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application
{
	public static AudioClip BUTTON_HOVER, CELL_SELECTED, SEE_CELL_SHOT, SHIP_HIT;
	
	private static Label playerLabel, cpuLabel, numOfActiveOfShipsPlayer, totalPointsPlayer, successfulShotsPlayer, numOfActiveOfShipsCpu, totalPointsCpu, successfulShotsCpu;
	private static Button shootButton;
	private static TextField textField;
	private static GridPane enemyGridPane;
	
	public static User player, cpu;
	
	private static CustomGridCell selectedEnemyGridCell = null;
	
	public static int selectedInputID = - 1;
	public static boolean mapIsLoaded, gameHasStarted;
	
	private LoadScenarioIDPopUp loadScenarioIDPopUp;
	private EnemyShipsPopUp enemyShipsPopUp;
	private LastShotsPopUp lastShotsPopUp;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	public void init()
	{
		mapIsLoaded = false;
		gameHasStarted = false;
		
		BUTTON_HOVER = new AudioClip(Main.class.getResource("/audio/hover.wav").toExternalForm());
		CELL_SELECTED = new AudioClip(Main.class.getResource("/audio/cell-selected.wav").toExternalForm());
		SEE_CELL_SHOT = new AudioClip(Main.class.getResource("/audio/see-cell-shot.wav").toExternalForm());
		SHIP_HIT = new AudioClip(Main.class.getResource("/audio/ship-hit.wav").toExternalForm());
		
		player = new User(true);
		cpu = new User(false);
	}
	
	public void start(Stage primaryStage)
	{
		// ------------- MENU BOX -------------
		
		Menu applicationMenu = new Menu("Application");
		MenuItem startMenu = new MenuItem("Start");
		MenuItem loadMenu = new MenuItem("Load");
		MenuItem exitMenu = new MenuItem("Exit");
		applicationMenu.getItems().addAll(startMenu, loadMenu, exitMenu);
		
		Menu detailsMenu = new Menu("Details");
		MenuItem enemyShipsMenu = new MenuItem("Enemy Ships");
		enemyShipsMenu.setOnAction(e ->
		{
			if(gameHasStarted)
				enemyShipsPopUp.showPopUp(cpu.getShips());
			else
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("MediaLab Battleship");
				alert.setHeaderText("Game Has Not Started Yet!");
				alert.setContentText("Load A Scenario-ID And Start A Game First.");
				alert.showAndWait();
			}
		});
		MenuItem playerShotsMenu = new MenuItem("Player Shots");
		playerShotsMenu.setOnAction(e ->
		{
			if(gameHasStarted)
				lastShotsPopUp.showPopUp(true, player.getFiveLastShots());
			else
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("MediaLab Battleship");
				alert.setHeaderText("Game Has Not Started Yet!");
				alert.setContentText("Load A Scenario-ID And Start A Game First.");
				alert.showAndWait();
			}
		});
		MenuItem enemyShotsMenu = new MenuItem("Enemy Shots");
		enemyShotsMenu.setOnAction(e ->
		{
			if(gameHasStarted)
				lastShotsPopUp.showPopUp(false, cpu.getFiveLastShots());
			else
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("MediaLab Battleship");
				alert.setHeaderText("Game Has Not Started Yet!");
				alert.setContentText("Load A Scenario-ID And Start A Game First.");
				alert.showAndWait();
			}
		});
		detailsMenu.getItems().addAll(enemyShipsMenu, playerShotsMenu, enemyShotsMenu);
		
		MenuBar menuBar = new MenuBar();
		menuBar.setCursor(Cursor.HAND);
		menuBar.getStyleClass().add("font-size-15");
		menuBar.getMenus().addAll(applicationMenu, detailsMenu);
		
		loadMenu.setOnAction((ActionEvent t) ->
		{
			selectedInputID = loadScenarioIDPopUp.showPopUp();
			if(selectedInputID > 0) InputFilesReader.readInputFiles();
		});
		
		exitMenu.setOnAction((ActionEvent t) ->
		{
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("MediaLab Battleship");
			alert.setHeaderText("Do you really want to exit?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) System.exit(0);
		});
		
		startMenu.setOnAction(e ->
		{
			if(mapIsLoaded) startGame();
			else
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("MediaLab Battleship");
				alert.setHeaderText("Game Is Not Loaded Yet!");
				alert.setContentText("Please Load A Game To Start.");
				alert.showAndWait();
			}
		});
		
		// ------------- TOP BOX -------------
		
		double GRID_SIZE = 840;
		
		// PLAYER INFO LABELS
		
		playerLabel = new Label("Player");
		playerLabel.getStyleClass().addAll("font-size-25", "padding");
		
		HBox playerLabelHBox = new HBox();
		playerLabelHBox.setAlignment(Pos.CENTER);
		playerLabelHBox.setPrefSize(GRID_SIZE, 60);
		playerLabelHBox.getChildren().add(playerLabel);
		
		numOfActiveOfShipsPlayer = new Label();
		numOfActiveOfShipsPlayer.getStyleClass().add("font-size-25");
		
		totalPointsPlayer = new Label();
		totalPointsPlayer.getStyleClass().add("font-size-25");
		
		successfulShotsPlayer = new Label();
		successfulShotsPlayer.getStyleClass().add("font-size-25");
		
		HBox topLeftInfoHBox = new HBox();
		topLeftInfoHBox.setAlignment(Pos.CENTER);
		topLeftInfoHBox.setSpacing(30);
		topLeftInfoHBox.setPrefHeight(50);
		topLeftInfoHBox.getChildren().addAll(numOfActiveOfShipsPlayer, totalPointsPlayer, successfulShotsPlayer);
		
		VBox topLeftVBox = new VBox();
		topLeftVBox.setAlignment(Pos.CENTER);
		topLeftVBox.getChildren().addAll(playerLabelHBox, topLeftInfoHBox);
		
		// CPU INFO LABELS
		
		cpuLabel = new Label("CPU");
		cpuLabel.getStyleClass().addAll("font-size-25", "padding");
		
		HBox cpuLabelHBox = new HBox();
		cpuLabelHBox.setAlignment(Pos.CENTER);
		cpuLabelHBox.setPrefSize(GRID_SIZE, 60);
		cpuLabelHBox.getChildren().add(cpuLabel);
		
		numOfActiveOfShipsCpu = new Label();
		numOfActiveOfShipsCpu.getStyleClass().add("font-size-25");
		
		totalPointsCpu = new Label();
		totalPointsCpu.getStyleClass().add("font-size-25");
		
		successfulShotsCpu = new Label();
		successfulShotsCpu.getStyleClass().add("font-size-25");
		
		HBox topRightInfoHBox = new HBox();
		topRightInfoHBox.setAlignment(Pos.CENTER);
		topRightInfoHBox.setSpacing(30);
		topRightInfoHBox.setPrefHeight(50);
		topRightInfoHBox.getChildren().addAll(numOfActiveOfShipsCpu, totalPointsCpu, successfulShotsCpu);
		
		VBox topRightVBox = new VBox();
		topRightVBox.setAlignment(Pos.CENTER);
		topRightVBox.getChildren().addAll(cpuLabelHBox, topRightInfoHBox);
		
		HBox topInfoHBox = new HBox();
		topInfoHBox.setPrefHeight(120);
		topInfoHBox.setAlignment(Pos.CENTER);
		topInfoHBox.setSpacing(50);
		topInfoHBox.getChildren().addAll(topLeftVBox, topRightVBox);
		
		// ------------- MIDDLE BOX -------------
		
		GridPane playersGridPane = new GridPane();
		playersGridPane.getStyleClass().add("border-2px-black");
		playersGridPane.setPrefSize(GRID_SIZE, GRID_SIZE);
		playersGridPane.setAlignment(Pos.CENTER);
		
		enemyGridPane = new GridPane();
		enemyGridPane.getStyleClass().add("border-2px-black");
		enemyGridPane.setPrefSize(GRID_SIZE, GRID_SIZE);
		enemyGridPane.setAlignment(Pos.CENTER);
		enemyGridPane.setDisable(true);
		
		for(int i = 0; i < 11; i++) // set row && column sizes
		{
			ColumnConstraints c = new ColumnConstraints();
			c.setPercentWidth(i == 0 ? 3 : 9.7);
			playersGridPane.getColumnConstraints().add(c);
			enemyGridPane.getColumnConstraints().add(c);
			
			RowConstraints r = new RowConstraints();
			r.setPercentHeight(i == 0 ? 3 : 9.7);
			playersGridPane.getRowConstraints().add(r);
			enemyGridPane.getRowConstraints().add(r);
		}
		
		// add empty label at (0, 0)
		double GRID_INDEX_CELL_SIZE = 0.03 * GRID_SIZE;
		double GRID_NORMAL_CELL_SIZE = 0.097 * GRID_SIZE;
		
		playersGridPane.add(new IndexLabel(GRID_INDEX_CELL_SIZE, GRID_INDEX_CELL_SIZE, ""), 0, 0);
		enemyGridPane.add(new IndexLabel(GRID_INDEX_CELL_SIZE, GRID_INDEX_CELL_SIZE, ""), 0, 0);
		
		for(int i = 0; i < 10; i++) // add horizontal && vertical indexes
		{
			playersGridPane.add(new IndexLabel(GRID_NORMAL_CELL_SIZE, GRID_INDEX_CELL_SIZE, String.format("%d", i + 1)), i + 1, 0);
			playersGridPane.add(new IndexLabel(GRID_INDEX_CELL_SIZE, GRID_NORMAL_CELL_SIZE, String.format("%d", i + 1)), 0, i + 1);
			
			enemyGridPane.add(new IndexLabel(GRID_NORMAL_CELL_SIZE, GRID_INDEX_CELL_SIZE, String.format("%d", i + 1)), i + 1, 0);
			enemyGridPane.add(new IndexLabel(GRID_INDEX_CELL_SIZE, GRID_NORMAL_CELL_SIZE, String.format("%d", i + 1)), 0, i + 1);
		}
		
		for(int xi = 0; xi < 10; xi++) // add labels to grids
		{
			for(int yi = 0; yi < 10; yi++)
			{
				player.getCells()[xi][yi] = new CustomGridCell(true, xi, yi);
				playersGridPane.add(player.getCells()[xi][yi], yi + 1, xi + 1);
				
				cpu.getCells()[xi][yi] = new CustomGridCell(false, xi, yi);
				cpu.getCells()[xi][yi].getStyleClass().add("border-2px-black");
				enemyGridPane.add(cpu.getCells()[xi][yi], yi + 1, xi + 1);
				
				int finalXi = xi;
				int finalYi = yi;
				cpu.getCells()[xi][yi].setOnMouseClicked(e ->
				{
					if(mapIsLoaded && cpu.getCells()[finalXi][finalYi].canShoot())
					{
						CELL_SELECTED.play();
						
						if(selectedEnemyGridCell != null)
							selectedEnemyGridCell.addNormalBorder();
						
						selectedEnemyGridCell = cpu.getCells()[finalXi][finalYi];
						selectedEnemyGridCell.addSelectedBorder();
						
						textField.setText("(" + (finalXi + 1) + "," + (finalYi + 1) + ")");
						
						shootButton.setDisable(false);
					}
				});
				cpu.getCells()[xi][yi].setOnMouseEntered(e ->
				{
					if(mapIsLoaded && cpu.getCells()[finalXi][finalYi].canShoot())
					{
						cpu.getCells()[finalXi][finalYi].setId("sea-cell-hover");
						enemyGridPane.getChildren().get((finalXi + 1) * 2).setId("sea-cell-hover");
						enemyGridPane.getChildren().get((finalYi + 1) * 2 - 1).setId("sea-cell-hover");
					}
				});
				cpu.getCells()[xi][yi].setOnMouseExited(e ->
				{
					if(mapIsLoaded && cpu.getCells()[finalXi][finalYi].canShoot())
					{
						cpu.getCells()[finalXi][finalYi].setId("sea-cell");
						enemyGridPane.getChildren().get((finalXi + 1) * 2).setId("");
						enemyGridPane.getChildren().get((finalYi + 1) * 2 - 1).setId("");
					}
				});
			}
		}
		
		HBox hBoxFor2Grids = new HBox();
		hBoxFor2Grids.setAlignment(Pos.CENTER);
		hBoxFor2Grids.setFillHeight(true);
		hBoxFor2Grids.setSpacing(50);
		hBoxFor2Grids.getChildren().addAll(playersGridPane, enemyGridPane);
		
		// ------------- BOTTOM BOX -------------
		
		Label shootLabel = new Label("Target Cell:");
		shootLabel.setPrefHeight(40);
		shootLabel.getStyleClass().add("font-size-20");
		
		textField = new TextField();
		textField.setPrefSize(90, 40);
		textField.getStyleClass().add("font-size-20");
		textField.setDisable(true);
		textField.setFocusTraversable(false);
		
		shootButton = new Button("Shoot");
		shootButton.setPrefSize(140, 40);
		shootButton.getStyleClass().add("font-size-20");
		shootButton.setCursor(Cursor.HAND);
		shootButton.setDisable(true);
		shootButton.setOnMouseEntered(e -> BUTTON_HOVER.play());
		shootButton.setOnAction(e ->
		{
			if(selectedEnemyGridCell != null)
			{
				textField.setText("");
				
				selectedEnemyGridCell.shotFired();
				
				selectedEnemyGridCell.addNormalBorder();
				selectedEnemyGridCell = null;
				
				updateStatsLabels(true);
				
				cpuTurn();
			}
		});
		
		HBox bottomHBox = new HBox();
		bottomHBox.setFillHeight(true);
		bottomHBox.setAlignment(Pos.CENTER);
		bottomHBox.setSpacing(20);
		bottomHBox.setPrefHeight(100);
		bottomHBox.getChildren().addAll(shootLabel, textField, shootButton);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topInfoHBox);
		borderPane.setCenter(hBoxFor2Grids);
		borderPane.setBottom(bottomHBox);
		
		VBox sceneContainerVBox = new VBox();
		sceneContainerVBox.getChildren().addAll(menuBar, borderPane);
		
		Scene scene = new Scene(sceneContainerVBox, 1920, 1080);
		scene.getStylesheets().add(Main.class.getResource("/css/styles.css").toExternalForm());
		
		primaryStage.setTitle("MediaLab Battleship");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		loadScenarioIDPopUp = new LoadScenarioIDPopUp();
		enemyShipsPopUp = new EnemyShipsPopUp();
		lastShotsPopUp = new LastShotsPopUp();
	}
	
	void startGame()
	{
		if(!gameHasStarted)
		{
			gameHasStarted = true;
			
			Random random = new Random();
			boolean playerStartsFirst = random.nextBoolean();
			
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("MediaLab Battleship");
			alert.setHeaderText(playerStartsFirst ? "You Start First!" : "CPU Starts First!");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK)
			{
				if(!playerStartsFirst)
					cpuTurn();
				else
					playerTurn();
			}
		}
		else
		{
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("MediaLab Battleship");
			alert.setHeaderText("Game Has Already Started!");
			alert.showAndWait();
		}
	}
	
	public static void updateStatsLabels(boolean playerDidShot)
	{
		if(playerDidShot)
		{
			numOfActiveOfShipsCpu.setText("Active Ships: " + cpu.getActiveShips() + "/5");
			totalPointsPlayer.setText("Total Points: " + player.getPoints());
			successfulShotsPlayer.setText("Successful Shots: " + player.getSuccessfulShots() + "/" + player.getShotsFired());
		}
		else
		{
			numOfActiveOfShipsPlayer.setText("Active Ships: " + player.getActiveShips() + "/5");
			totalPointsCpu.setText("Total Points: " + cpu.getPoints());
			successfulShotsCpu.setText("Successful Shots: " + cpu.getSuccessfulShots() + "/" + cpu.getShotsFired());
		}
	}
	
	public static void resetStatsLabels()
	{
		numOfActiveOfShipsPlayer.setText("");
		totalPointsPlayer.setText("");
		successfulShotsPlayer.setText("");
		numOfActiveOfShipsCpu.setText("");
		totalPointsCpu.setText("");
		successfulShotsCpu.setText("");
	}
	
	public static void resetGrids()
	{
		player.resetCells();
		cpu.resetCells();
	}
	
	public static void playerTurn()
	{
		if(hasGameEnded())
			gameHasEnded();
		else
		{
			enemyGridPane.setDisable(false);
			shootButton.setDisable(false);
			
			cpuLabel.setId("");
			playerLabel.setId("user-border");
		}
	}
	
	public static void cpuTurn()
	{
		enemyGridPane.setDisable(true);
		shootButton.setDisable(true);
		
		if(hasGameEnded())
			gameHasEnded();
		else
		{
			playerLabel.setId("");
			cpuLabel.setId("user-border");
			
			cpu.getAI().playNextTurn(player.getCells());
		}
	}
	
	private static boolean hasGameEnded()
	{
		return player.getActiveShips() == 0 || cpu.getActiveShips() == 0 || (player.getShotsFired() == User.MAX_SHOTS_ALLOWED && cpu.getShotsFired() == User.MAX_SHOTS_ALLOWED);
	}
	
	private static void gameHasEnded()
	{
		playerLabel.setId("");
		cpuLabel.setId("");
		
		boolean playerWon = (cpu.getActiveShips() == 0 && player.getActiveShips() > 0) || player.getPoints() > cpu.getPoints();
		
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("MediaLab Battleship");
		alert.setHeaderText("Game Has Ended!");
		alert.setContentText(playerWon ? "You Won!!!" : "CPU Won :(");
		Optional<ButtonType> result = alert.showAndWait();
	}
}






