package com.johnzaro_nikosece.medialab_battleship.popups;

import com.johnzaro_nikosece.medialab_battleship.Main;
import com.johnzaro_nikosece.medialab_battleship.dataStructures.ship.Ship;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class EnemyShipsPopUp
{
	private Stage stage;
	private TableView<Ship> tableView;
	
	public EnemyShipsPopUp()
	{
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("MediaLab Battleship");
		stage.setWidth(600);
		stage.setHeight(400);
		stage.setResizable(false);
		
		Label titleLabel = new Label("Enemy Ships");
		titleLabel.getStyleClass().add("font-size-20");
		
		TableColumn<Ship, String> shipNameColumn = new TableColumn<>("Ship Type");
		shipNameColumn.setSortable(false);
		shipNameColumn.setReorderable(false);
		shipNameColumn.setEditable(false);
		shipNameColumn.setCellValueFactory(param -> param.getValue().shipNameProperty());
		
		TableColumn<Ship, String> shipSizeColumn = new TableColumn<>("Ship Size");
		shipSizeColumn.setSortable(false);
		shipSizeColumn.setReorderable(false);
		shipSizeColumn.setEditable(false);
		shipSizeColumn.setCellValueFactory(param -> param.getValue().shipSizeStringProperty());
		
		TableColumn<Ship, String> timesHitColumn = new TableColumn<>("Times Hit");
		timesHitColumn.setSortable(false);
		timesHitColumn.setReorderable(false);
		timesHitColumn.setEditable(false);
		timesHitColumn.setCellValueFactory(param -> param.getValue().timesHitProperty());
		
		TableColumn<Ship, String> shipStateColumn = new TableColumn<>("Ship State");
		shipStateColumn.setSortable(false);
		shipStateColumn.setReorderable(false);
		shipStateColumn.setCellValueFactory(param -> param.getValue().shipStateStringProperty());
		
		tableView = new TableView<>();
		tableView.setFocusTraversable(false);
		tableView.setEditable(false);
		tableView.setSelectionModel(null);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.getColumns().setAll(shipNameColumn, shipSizeColumn, timesHitColumn, shipStateColumn);
		
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
	
	public void showPopUp(List<Ship> items)
	{
		tableView.setItems(FXCollections.observableList(items));
		
		stage.showAndWait();
	}
}
