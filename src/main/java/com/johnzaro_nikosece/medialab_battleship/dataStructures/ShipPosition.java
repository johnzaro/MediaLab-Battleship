package com.johnzaro_nikosece.medialab_battleship.dataStructures;

public class ShipPosition
{
	private Position position;
	private boolean isHorizontal;
	
	public ShipPosition(int x, int y, boolean isHorizontal)
	{
		position = new Position(x, y);
		this.isHorizontal = isHorizontal;
	}
	
	public int getX()
	{
		return position.getX();
	}
	
	public void setX(int x)
	{
		position.setX(x);
	}
	
	public int getY()
	{
		return position.getY();
	}
	
	public void setY(int y)
	{
		position.setY(y);
	}
	
	public boolean isHorizontal()
	{
		return isHorizontal;
	}
}
