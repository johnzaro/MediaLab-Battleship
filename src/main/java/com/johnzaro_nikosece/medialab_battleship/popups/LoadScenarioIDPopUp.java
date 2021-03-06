package com.johnzaro_nikosece.medialab_battleship.popups;

import com.johnzaro_nikosece.medialab_battleship.Main;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoadScenarioIDPopUp
{
	private Stage stage;
	
	private int id;
	
	public LoadScenarioIDPopUp()
	{
		Button loadButton;
		
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("MediaLab Battleship");
		stage.setWidth(350);
		stage.setHeight(200);
		stage.setResizable(false);
		
		Label label = new Label("Provide a SCENARIO-ID:");
		label.getStyleClass().add("font-size-20");
		
		TextField nameInput = new TextField();
		nameInput.getStyleClass().add("font-size-20");
		nameInput.setPrefSize(90, 35);
		
		Tooltip tooltip = new Tooltip("The ID Must Be An Integer Between 1 And 99999");
		tooltip.setPrefWidth(250);
		tooltip.setShowDelay(Duration.ZERO);
		tooltip.setHideDelay(Duration.ZERO);
		tooltip.setShowDuration(Duration.INDEFINITE);
		tooltip.setWrapText(true);
		tooltip.setHideOnEscape(false);
		nameInput.setTooltip(tooltip);
		
		nameInput.textProperty().addListener((observable, oldValue, newValue) ->
		{
			if(!newValue.matches("\\d{1,5}"))
				nameInput.setText(oldValue);
		});
		
		HBox inputHBox = new HBox(20);
		inputHBox.setAlignment(Pos.CENTER);
		inputHBox.getChildren().addAll(label, nameInput);
		
		Button cancelButton = new Button("Cancel");
		cancelButton.getStyleClass().add("font-size-20");
		cancelButton.setCursor(Cursor.HAND);
		cancelButton.setPrefSize(120, 35);
		cancelButton.setOnAction(e ->
		{
			id = -1;
			stage.close();
		});
		
		loadButton = new Button("Load");
		loadButton.getStyleClass().add("font-size-20");
		loadButton.setCursor(Cursor.HAND);
		loadButton.setPrefSize(120, 35);
		loadButton.setOnAction(e ->
		{
			try
			{
				if(!nameInput.getText().isEmpty())
				{
					int temp = Integer.parseInt(nameInput.getText());
					if(temp > 0)
						id = temp;
					else
						id = -1;
					
					stage.close();
				}
			}
			catch(NumberFormatException ignored) { }
		});
		
		nameInput.setOnAction(e -> loadButton.fire());
		
		HBox buttonsHBox = new HBox(20);
		buttonsHBox.getChildren().addAll(cancelButton, loadButton);
		buttonsHBox.setAlignment(Pos.CENTER);
		
		VBox vBox = new VBox(30);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(inputHBox, buttonsHBox);
		
		Scene scene = new Scene(vBox);
		scene.getStylesheets().add(Main.class.getResource("/css/styles.css").toExternalForm());
		stage.setScene(scene);
	}
	
	public int showPopUp()
	{
		stage.showAndWait();
		
		return id;
	}
}
