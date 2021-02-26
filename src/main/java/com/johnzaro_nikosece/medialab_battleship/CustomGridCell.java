package com.johnzaro_nikosece.medialab_battleship;

import javafx.scene.Cursor;
import javafx.scene.control.Label;

public class CustomGridCell extends Label
{
	final String BLUE_COLOR = "#3498db";
	final String CYAN_COLOR = "#66D5D5";
	final String CARRIER_COLOR = "#9b59b6";
	final String BATTLESHIP_COLOR = "#A6A6A6";
	final String CRUISER_COLOR = "#ED7D31";
	final String SUBMARINE_COLOR = "#FEFF02";
	final String DESTROYER_COLOR = "#A8D08E";
	
	boolean isEmptyCell;
	boolean isPlayerCell;
	
	private Ship ship;
	
	CustomGridCell(boolean isPlayerCell)
	{
		setStyle("-fx-background-color: " + BLUE_COLOR + ";");
		isEmptyCell = true;
		this.isPlayerCell = isPlayerCell;
		
		setPrefSize(86, 86);
		
		if(!isPlayerCell)
		{
			setCursor(Cursor.HAND);
			setOnMouseEntered(e -> setStyle("-fx-background-color: " + CYAN_COLOR + ";"));
			setOnMouseExited(e -> setStyle("-fx-background-color: " + BLUE_COLOR + ";"));
		}
	}
	
	public void setShip(Ship ship)
	{
		this.ship = ship;
		isEmptyCell = false;
		
		if(isPlayerCell)
		{
			String color = switch(ship.shipType)
			{
				case CARRIER -> CARRIER_COLOR;
				case BATTLESHIP -> BATTLESHIP_COLOR;
				case CRUISER -> CRUISER_COLOR;
				case SUBMARINE -> SUBMARINE_COLOR;
				case DESTROYER -> DESTROYER_COLOR;
				default -> "";
			};
			
			setStyle("-fx-background-color: " + color + ";");
		}
	}
	
	public void resetCell()
	{
		setStyle("-fx-background-color: " + BLUE_COLOR + ";");
		isEmptyCell = true;
	}
}
