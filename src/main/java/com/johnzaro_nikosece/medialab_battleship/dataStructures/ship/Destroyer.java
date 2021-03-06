package com.johnzaro_nikosece.medialab_battleship.dataStructures.ship;

import com.johnzaro_nikosece.medialab_battleship.dataStructures.ShipPosition;

public class Destroyer extends Ship
{
	public Destroyer(int x, int y, boolean isHorizontal)
	{
		super();
		
		setShipType(ShipType.DESTROYER);
		setShipState(ShipState.OPERATING);
		
		setShipSize(2);
		setSuccessfulShotPoints(50);
		setBonusPoints(0);
		
		setCssClass("destroyer-cell");
		
		setShipPosition(new ShipPosition(x, y, isHorizontal));
	}
}
