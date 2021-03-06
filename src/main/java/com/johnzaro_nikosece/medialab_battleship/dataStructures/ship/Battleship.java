package com.johnzaro_nikosece.medialab_battleship.dataStructures.ship;

import com.johnzaro_nikosece.medialab_battleship.dataStructures.ShipPosition;

public class Battleship extends Ship
{
	public Battleship(int x, int y, boolean isHorizontal)
	{
		super();
		
		setShipType(ShipType.BATTLESHIP);
		
		setShipSize(4);
		setSuccessfulShotPoints(250);
		setBonusPoints(500);
		
		setCssClass("battleship-cell");
		
		setShipPosition(new ShipPosition(x, y, isHorizontal));
	}
}
