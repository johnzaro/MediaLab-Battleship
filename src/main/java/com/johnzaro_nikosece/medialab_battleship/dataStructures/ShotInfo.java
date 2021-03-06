package com.johnzaro_nikosece.medialab_battleship.dataStructures;

import com.johnzaro_nikosece.medialab_battleship.dataStructures.ship.Ship.ShipType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ShotInfo
{
	public ShotInfo(Position position, boolean isSuccessfulShot, ShipType shipType)
	{
		positionString = new SimpleStringProperty();
		positionString.set(String.format("(%d, %d)", position.getX() + 1, position.getY() + 1));
		
		isSuccessfulShotString = new SimpleStringProperty();
		isSuccessfulShotString.set(isSuccessfulShot ? "Yes" : "No");
		
		shipTypeString = new SimpleStringProperty();
		if(shipType == ShipType.BATTLESHIP)
			shipTypeString.set("Battleship");
		else if(shipType == ShipType.CARRIER)
			shipTypeString.set("Carrier");
		else if(shipType == ShipType.CRUISER)
			shipTypeString.set("Cruiser");
		else if(shipType == ShipType.DESTROYER)
			shipTypeString.set("Destroyer");
		else if(shipType == ShipType.SUBMARINE)
			shipTypeString.set("Submarine");
		else
			shipTypeString.set("-");
	}
	
	private final StringProperty positionString;
	public StringProperty positionProperty()
	{
		return positionString;
	}
	
	private final StringProperty isSuccessfulShotString;
	public StringProperty isSuccessfulShotProperty()
	{
		return isSuccessfulShotString;
	}
	
	private final StringProperty shipTypeString;
	public StringProperty shipTypeProperty()
	{
		return shipTypeString;
	}
}
