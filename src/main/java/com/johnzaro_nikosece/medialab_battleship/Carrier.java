package com.johnzaro_nikosece.medialab_battleship;

public class Carrier extends Ship
{
	Carrier(int x, int y, boolean isHorizontal)
	{
		typeOfShip = 1;
		shipSize = 5;
		successfulShotPoints = 350;
		bonusPoints = 1000;
		
		position = new Position(x, y, isHorizontal);
	}
}
