package com.johnzaro_nikosece.medialab_battleship;

import java.util.ArrayList;
import java.util.List;

public class User
{
	private static final int MAX_SHOTS_ALLOWED = 40;
	
	private int points;
	private int shotsFired;
	private int successfulShots;
	private int activeShips;
	
	private List<Position> fiveLastShots;
	
	User()
	{
		points = 0;
		shotsFired = 0;
		successfulShots = 0;
		activeShips = 0;
		
		fiveLastShots = new ArrayList<>();
	}
	
	public static int getMaxShotsAllowed()
	{
		return MAX_SHOTS_ALLOWED;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public void setPoints(int points)
	{
		this.points = points;
	}
	
	public void addPoints(int newPoints)
	{
		points += newPoints;
	}
	
	public int getShotsFired()
	{
		return shotsFired;
	}
	
	public void setShotsFired(int shotsFired)
	{
		this.shotsFired = shotsFired;
	}
	
	public int getSuccessfulShots()
	{
		return successfulShots;
	}
	
	public void setSuccessfulShots(int successfulShots)
	{
		this.successfulShots = successfulShots;
	}
	
	public int getActiveShips()
	{
		return activeShips;
	}
	
	public void setActiveShips(int activeShips)
	{
		this.activeShips = activeShips;
	}
}
