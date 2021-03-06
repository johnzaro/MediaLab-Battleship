package com.johnzaro_nikosece.medialab_battleship.popups;

import com.johnzaro_nikosece.medialab_battleship.Main;
import com.johnzaro_nikosece.medialab_battleship.dataStructures.ShotInfo;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class LastShotsPopUp
{
	private Stage stage;
	private Label titleLabel;
	private TableView<ShotInfo> tableView;
	
	public LastShotsPopUp()
	{
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("MediaLab Battleship");
		stage.setWidth(600);
		stage.setHeight(400);
		stage.setResizable(false);
		
		titleLabel = new Label();
		titleLabel.getStyleClass().add("font-size-20");
		
		TableColumn<ShotInfo, String> positionColumn = new TableColumn<>("Position");
		positionColumn.setSortable(false);
		positionColumn.setReorderable(false);
		positionColumn.setEditable(false);
		positionColumn.setCellValueFactory(param -> param.getValue().positionProperty());
		
		TableColumn<ShotInfo, String> isSuccessfulShotColumn = new TableColumn<>("Successful Shot");
		isSuccessfulShotColumn.setSortable(false);
		isSuccessfulShotColumn.setReorderable(false);
		isSuccessfulShotColumn.setEditable(false);
		isSuccessfulShotColumn.setCellValueFactory(param -> param.getValue().isSuccessfulShotProperty());
		
		TableColumn<ShotInfo, String> shipTypeColumn = new TableColumn<>("Ship Type");
		shipTypeColumn.setSortable(false);
		shipTypeColumn.setReorderable(false);
		shipTypeColumn.setEditable(false);
		shipTypeColumn.setCellValueFactory(param -> param.getValue().shipTypeProperty());
		
		tableView = new TableView<>();
		tableView.setFocusTraversable(false);
		tableView.setEditable(false);
		tableView.setSelectionModel(null);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.getColumns().setAll(positionColumn, isSuccessfulShotColumn, shipTypeColumn);
		
		Tooltip tooltip = new Tooltip("The Table Shows A Maximum Of 5 Shots. The Most Recent Shot Appears At The Top And The Least Recent One At The Bottom.");
		tooltip.setPrefWidth(300);
		tooltip.getStyleClass().add("font-size-15");
		tooltip.setHideDelay(Duration.ZERO);
		tooltip.setShowDuration(Duration.INDEFINITE);
		tooltip.setWrapText(true);
		tooltip.setHideOnEscape(false);
		tableView.setTooltip(tooltip);
		
		Button closeButton = new Button("Close");
		closeButton.getStyleClass().add("font-size-20");
		closeButton.setCursor(Cursor.HAND);
		closeButton.setPrefSize(120, 35);
		closeButton.setOnAction(e -> stage.close());
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(titleLabel);
		borderPane.setCenter(tableView);
		borderPane.setBottom(closeButton);
		BorderPane.setAlignment(titleLabel, Pos.CENTER);
		BorderPane.setMargin(titleLabel, new Insets(20,0,0,0));
		BorderPane.setMargin(tableView, new Insets(20,20,20,20));
		BorderPane.setAlignment(closeButton, Pos.CENTER);
		BorderPane.setMargin(closeButton, new Insets(0,0,20,0));
		
		Scene scene = new Scene(borderPane);
		scene.getStylesheets().add(Main.class.getResource("/css/styles.css").toExternalForm());
		stage.setScene(scene);
	}
	
	public void showPopUp(boolean showPlayerShots, List<ShotInfo> items)
	{
		titleLabel.setText("Most Recent " + (showPlayerShots ? "Player" : "Enemy") + " Shots");
		
		tableView.setItems(FXCollections.observableList(items));
		
		stage.showAndWait();
	}
}
