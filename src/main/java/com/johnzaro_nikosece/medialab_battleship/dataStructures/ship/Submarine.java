package com.johnzaro_nikosece.medialab_battleship.dataStructures.ship;

import com.johnzaro_nikosece.medialab_battleship.dataStructures.ShipPosition;

public class Submarine extends Ship
{
	public Submarine(int x, int y, boolean isHorizontal)
	{
		super();
		
		setShipType(ShipType.SUBMARINE);
		setShipState(ShipState.OPERATING);
		
		setShipSize(3);
		setSuccessfulShotPoints(100);
		setBonusPoints(0);
		
		setCssClass("submarine-cell");
		
		setShipPosition(new ShipPosition(x, y, isHorizontal));
	}
}
