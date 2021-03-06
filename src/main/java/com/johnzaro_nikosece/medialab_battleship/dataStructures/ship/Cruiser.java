package com.johnzaro_nikosece.medialab_battleship.dataStructures.ship;

import com.johnzaro_nikosece.medialab_battleship.dataStructures.ShipPosition;

public class Cruiser extends Ship
{
	public Cruiser(int x, int y, boolean isHorizontal)
	{
		super();
		
		setShipType(ShipType.CRUISER);
		setShipState(ShipState.OPERATING);
		
		setShipSize(3);
		setSuccessfulShotPoints(100);
		setBonusPoints(250);
		
		setCssClass("cruiser-cell");
		
		setShipPosition(new ShipPosition(x, y, isHorizontal));
	}
}
