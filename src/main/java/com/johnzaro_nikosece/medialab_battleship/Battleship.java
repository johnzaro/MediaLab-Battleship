package com.johnzaro_nikosece.medialab_battleship;

public class Battleship extends Ship
{
	Battleship(int x, int y, boolean isHorizontal)
	{
		typeOfShip = 2;
		shipSize = 4;
		successfulShotPoints = 250;
		bonusPoints = 500;
		
		position = new Position(x, y, isHorizontal);
	}
}
