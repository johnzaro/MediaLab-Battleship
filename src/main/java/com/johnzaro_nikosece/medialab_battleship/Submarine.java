package com.johnzaro_nikosece.medialab_battleship;

public class Submarine extends Ship
{
	Submarine(int x, int y, boolean isHorizontal)
	{
		typeOfShip = 4;
		shipSize = 3;
		successfulShotPoints = 100;
		bonusPoints = 0;
		
		position = new Position(x, y, isHorizontal);
	}
}
