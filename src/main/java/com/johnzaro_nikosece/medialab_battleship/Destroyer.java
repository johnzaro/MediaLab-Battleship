package com.johnzaro_nikosece.medialab_battleship;

public class Destroyer extends Ship
{
	Destroyer(int x, int y, boolean isHorizontal)
	{
		typeOfShip = 5;
		shipSize = 2;
		successfulShotPoints = 50;
		bonusPoints = 0;
		
		position = new Position(x, y, isHorizontal);
	}
}
