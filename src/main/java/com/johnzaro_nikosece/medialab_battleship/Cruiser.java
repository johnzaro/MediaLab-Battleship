package com.johnzaro_nikosece.medialab_battleship;

public class Cruiser extends Ship
{
	Cruiser(int x, int y, boolean isHorizontal)
	{
		typeOfShip = 3;
		shipSize = 3;
		successfulShotPoints = 100;
		bonusPoints = 250;
		
		position = new Position(x, y, isHorizontal);
	}
}
