package com.johnzaro_nikosece.medialab_battleship.dataStructures.ship;

import com.johnzaro_nikosece.medialab_battleship.customControls.CustomGridCell;
import com.johnzaro_nikosece.medialab_battleship.dataStructures.ShipPosition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public abstract class Ship
{
	public enum ShipType {CARRIER, BATTLESHIP, CRUISER, SUBMARINE, DESTROYER}
	public enum ShipState {OPERATING, HIT, SHANK}
	
	//Variables
	
	private ShipType shipType;
	private ShipState shipState;
	
	private int shipSize;
	
	private int successfulShotPoints;
	private int bonusPoints;
	
	private int timesHitCounter;
	
	private ShipPosition shipPosition;
	
	private String cssClass;
	
	private List<CustomGridCell> usedGridCells;
	
	private final StringProperty shipSizeString;
	public void setShipSizeString(String name)
	{
		shipSizeStringProperty().set(name);
	}
	public StringProperty shipSizeStringProperty()
	{
		return shipSizeString;
	}
	
	private final StringProperty shipStateString;
	public void setShipStateString(String name)
	{
		shipStateStringProperty().set(name);
	}
	public StringProperty shipStateStringProperty()
	{
		return shipStateString;
	}
	
	private final StringProperty shipNameString;
	public void setShipNameString(String name)
	{
		shipNameProperty().set(name);
	}
	public StringProperty shipNameProperty()
	{
		return shipNameString;
	}
	
	private final StringProperty timesHitString;
	public void setTimeHitString(String name)
	{
		timesHitProperty().set(name);
	}
	public StringProperty timesHitProperty()
	{
		return timesHitString;
	}
	
	Ship()
	{
		shipSizeString = new SimpleStringProperty();
		shipStateString = new SimpleStringProperty();
		shipNameString = new SimpleStringProperty();
		timesHitString = new SimpleStringProperty();
		
		setShipState(ShipState.OPERATING);
		timesHitCounter = 0;
		setTimeHitString("0");
	}
	
	public int getTimesHitCounter()
	{
		return timesHitCounter;
	}
	
	public void increaseTimesHitCounter()
	{
		timesHitCounter++;
		setTimeHitString("" + timesHitCounter);
	}
	
	public int getShipSize()
	{
		return shipSize;
	}
	
	public void setShipSize(int shipSize)
	{
		this.shipSize = shipSize;
		setShipSizeString("" + shipSize);
	}
	
	public int getSuccessfulShotPoints()
	{
		return successfulShotPoints;
	}
	
	public void setSuccessfulShotPoints(int successfulShotPoints)
	{
		this.successfulShotPoints = successfulShotPoints;
	}
	
	public int getBonusPoints()
	{
		return bonusPoints;
	}
	
	public void setBonusPoints(int bonusPoints)
	{
		this.bonusPoints = bonusPoints;
	}
	
	public ShipType getShipType()
	{
		return shipType;
	}
	
	public void setShipType(ShipType shipType)
	{
		this.shipType = shipType;
		
		if(shipType == ShipType.BATTLESHIP)
			setShipNameString("Battleship");
		else if(shipType == ShipType.CARRIER)
			setShipNameString("Carrier");
		else if(shipType == ShipType.CRUISER)
			setShipNameString("Cruiser");
		else if(shipType == ShipType.DESTROYER)
			setShipNameString("Destroyer");
		else
			setShipNameString("Submarine");
	}
	
	public ShipState getShipState()
	{
		return shipState;
	}
	
	public void setShipState(ShipState shipState)
	{
		this.shipState = shipState;
		
		if(shipState == ShipState.OPERATING)
			setShipStateString("Operating");
		else if(shipState == ShipState.HIT)
			setShipStateString("Hit");
		else
			setShipStateString("Shank");
	}
	
	public List<CustomGridCell> getUsedGridCells()
	{
		return usedGridCells;
	}
	
	public void setUsedGridCells(List<CustomGridCell> usedGridCells)
	{
		this.usedGridCells = usedGridCells;
	}
	
	public String getCssClass()
	{
		return cssClass;
	}
	
	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}
	
	public ShipPosition getShipPosition()
	{
		return shipPosition;
	}
	
	public void setShipPosition(ShipPosition shipPosition)
	{
		this.shipPosition = shipPosition;
	}
}
