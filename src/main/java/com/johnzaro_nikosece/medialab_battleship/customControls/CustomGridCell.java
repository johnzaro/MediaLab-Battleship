package com.johnzaro_nikosece.medialab_battleship.customControls;

import com.johnzaro_nikosece.medialab_battleship.Main;
import com.johnzaro_nikosece.medialab_battleship.dataStructures.Position;
import com.johnzaro_nikosece.medialab_battleship.dataStructures.ShotInfo;
import com.johnzaro_nikosece.medialab_battleship.dataStructures.ship.Ship;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class CustomGridCell extends Label
{
	public enum ShotResult {SEE_CELL_SHOT, SHIP_HIT, SHIP_SHANK}
	
	private final Position position;
	private Ship ship;
	
	private final boolean isPlayerCell;
	private boolean isEmptyCell;
	private boolean canShoot;
	
	public CustomGridCell(boolean isPlayerCell, int x, int y)
	{
		position = new Position(x, y);
		
		getStyleClass().add("border-2px-black");
		setId("sea-cell");
		
		this.isPlayerCell = isPlayerCell;
		isEmptyCell = true;
		canShoot = true;
		
		setPrefSize(85, 85);
		
		GridPane.setHalignment(this, HPos.CENTER);
		GridPane.setValignment(this, VPos.CENTER);
		
		if(!isPlayerCell)
			setCursor(Cursor.HAND);
	}
	
	public ShotResult shotFired()
	{
		canShoot = false;
		setCursor(Cursor.DEFAULT);
		
		if(isPlayerCell)
			Main.cpu.incrementShotsFired();
		else
			Main.player.incrementShotsFired();
		
		ShotResult result;
		
		if(!isEmptyCell)
		{
			Main.SHIP_HIT.play();
			
			ship.increaseTimesHitCounter();
			
			if(isPlayerCell)
			{
				Main.cpu.incrementSuccessfulShots();
				Main.cpu.addPoints(ship.getSuccessfulShotPoints());
				Main.cpu.getFiveLastShots().addElement(new ShotInfo(getPosition(), true, ship.getShipType()));
			}
			else
			{
				Main.player.incrementSuccessfulShots();
				Main.player.addPoints(ship.getSuccessfulShotPoints());
				Main.player.getFiveLastShots().addElement(new ShotInfo(getPosition(), true, ship.getShipType()));
			}
			
			if(ship.getTimesHitCounter() == ship.getShipSize())
			{
				result = ShotResult.SHIP_SHANK;
				
				if(isPlayerCell)
				{
					Main.player.decrementActiveShips();
					Main.cpu.addPoints(ship.getBonusPoints());
					
					for(CustomGridCell cell: ship.getUsedGridCells())
						cell.ship.setShipState(Ship.ShipState.SHANK);
					
					setId("ship-hit");
				}
				else
				{
					Main.cpu.decrementActiveShips();
					Main.player.addPoints(ship.getBonusPoints());
					
					for(CustomGridCell cell: ship.getUsedGridCells())
					{
						cell.ship.setShipState(Ship.ShipState.SHANK);
						cell.setId(cell.ship.getCssClass());
					}
				}
			}
			else
			{
				ship.setShipState(Ship.ShipState.HIT);
				result = ShotResult.SHIP_HIT;
				setId("ship-hit");
			}
		}
		else
		{
			result = ShotResult.SEE_CELL_SHOT;
			setId("sea-cell-shot");
			Main.SEE_CELL_SHOT.play();
			
			if(isPlayerCell)
				Main.cpu.getFiveLastShots().addElement(new ShotInfo(getPosition(), false, null));
			else
				Main.player.getFiveLastShots().addElement(new ShotInfo(getPosition(), false, null));
				
		}
		
		return result;
	}
	
	public void setShip(Ship ship)
	{
		this.ship = ship;
		isEmptyCell = false;
		
		if(isPlayerCell)
		{
			setId(ship.getCssClass());
		}
	}
	
	public void resetCell()
	{
		setId("sea-cell");
		addNormalBorder();
		
		isEmptyCell = true;
		canShoot = true;
		ship = null;
	}
	
	public List<CustomGridCell> getHorizontalNeighborsThatCanBeHit()
	{
		List<CustomGridCell> neighbors = new ArrayList<>();
		CustomGridCell testCell;
		
		if(position.getY() > 0)
		{
			testCell = Main.player.getCells()[position.getX()][position.getY() - 1];
			if(testCell.canShoot)
				neighbors.add(testCell);
		}
		if(position.getY() < 9)
		{
			testCell = Main.player.getCells()[position.getX()][position.getY() + 1];
			if(testCell.canShoot)
				neighbors.add(testCell);
		}
		
		return neighbors;
	}
	
	public List<CustomGridCell> getVerticalNeighborsThatCanBeHit()
	{
		List<CustomGridCell> neighbors = new ArrayList<>();
		CustomGridCell testCell;
		
		if(position.getX() > 0)
		{
			testCell = Main.player.getCells()[position.getX() - 1][position.getY()];
			if(testCell.canShoot)
				neighbors.add(testCell);
		}
		if(position.getX() < 9)
		{
			testCell = Main.player.getCells()[position.getX() + 1][position.getY()];
			if(testCell.canShoot)
				neighbors.add(testCell);
		}
		
		return neighbors;
	}
	
	public Position getPosition()
	{
		return position;
	}
	
	public void addNormalBorder()
	{
		getStyleClass().remove("border-4px-black");
		getStyleClass().add("border-2px-black");
	}
	
	public void addSelectedBorder()
	{
		getStyleClass().remove("border-2px-black");
		getStyleClass().add("border-4px-black");
	}
	
	public boolean canShoot()
	{
		return canShoot;
	}
	
	public boolean isEmptyCell()
	{
		return isEmptyCell;
	}
}
