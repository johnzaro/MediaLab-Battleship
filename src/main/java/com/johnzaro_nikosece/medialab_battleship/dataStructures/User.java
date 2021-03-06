package com.johnzaro_nikosece.medialab_battleship.dataStructures;

import com.johnzaro_nikosece.medialab_battleship.CPU_AI;
import com.johnzaro_nikosece.medialab_battleship.customControls.CustomGridCell;
import com.johnzaro_nikosece.medialab_battleship.dataStructures.ship.Ship;

import java.util.ArrayList;
import java.util.List;

public class User
{
	public static final int MAX_SHOTS_ALLOWED = 40;
	
	private int points;
	private int shotsFired;
	private int successfulShots;
	private int activeShips;
	
	private CPU_AI ai;
	
	private List<Ship> ships;
	private final BoundedList<ShotInfo> fiveLastShots;
	private final CustomGridCell[][] cells;
	
	public User(boolean isHuman)
	{
		resetStats();
		
		ships = new ArrayList<>();
		fiveLastShots = new BoundedList<>(5);
		cells = new CustomGridCell[10][10];
		
		if(!isHuman)
			ai = new CPU_AI();
	}
	
	public void resetUser()
	{
		resetStats();
		
		ships.clear();
		fiveLastShots.clear();
		resetCells();
	}
	
	private void resetStats()
	{
		points = 0;
		shotsFired = 0;
		successfulShots = 0;
		activeShips = 5;
	}
	
	public void resetCells()
	{
		for(int xi = 0; xi < 10; xi++) // add labels to grids
		{
			for(int yi = 0; yi < 10; yi++)
			{
				cells[xi][yi].resetCell();
				cells[xi][yi].resetCell();
			}
		}
	}
	
	public List<Ship> getShips()
	{
		return ships;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public void addPoints(int newPoints)
	{
		points += newPoints;
	}
	
	public int getShotsFired()
	{
		return shotsFired;
	}
	
	public void incrementShotsFired()
	{
		shotsFired++;
	}
	
	public int getSuccessfulShots()
	{
		return successfulShots;
	}
	
	public void incrementSuccessfulShots()
	{
		successfulShots++;
	}
	
	public int getActiveShips()
	{
		return activeShips;
	}
	
	public void decrementActiveShips()
	{
		activeShips--;
	}
	
	public CustomGridCell[][] getCells()
	{
		return cells;
	}
	
	public CPU_AI getAI()
	{
		return ai;
	}
	
	public BoundedList<ShotInfo> getFiveLastShots()
	{
		return fiveLastShots;
	}
}
