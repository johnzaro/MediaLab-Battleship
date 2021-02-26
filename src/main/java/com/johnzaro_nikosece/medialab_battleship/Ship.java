package com.johnzaro_nikosece.medialab_battleship;

public abstract class Ship
{
	public enum ShipType {CARRIER, BATTLESHIP, CRUISER, SUBMARINE, DESTROYER}
	public enum ShipState {OPERATING, HIT, SHANK}
	
	int typeOfShip;
	int shipSize;
	int successfulShotPoints;
	int bonusPoints;
	
	ShipType shipType;
	ShipState shipState;
	
	Position position;
}
