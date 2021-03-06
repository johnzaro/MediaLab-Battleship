package com.johnzaro_nikosece.medialab_battleship.dataStructures.ship;

import com.johnzaro_nikosece.medialab_battleship.dataStructures.ShipPosition;

public class Carrier extends Ship
{
	public Carrier(int x, int y, boolean isHorizontal)
	{
		super();
		
		setShipType(ShipType.CARRIER);
		setShipState(ShipState.OPERATING);
		
		setShipSize(5);
		setSuccessfulShotPoints(350);
		setBonusPoints(1000);
		
		setCssClass("carrier-cell");
		
		setShipPosition(new ShipPosition(x, y, isHorizontal));
	}
}
