package com.johnzaro_nikosece.medialab_battleship;

import com.johnzaro_nikosece.medialab_battleship.customControls.CustomGridCell;
import com.johnzaro_nikosece.medialab_battleship.customControls.CustomGridCell.ShotResult;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CPU_AI
{
	private final Random random;
	
	private enum State {HAVE_NOT_FOUND_SHIP, SHIP_HIT_FIRST_TIME, SHIP_HIT_SECOND_TIME}
	private State state;
	
	private List<CustomGridCell> horizontalCellNeighbors, verticalCellNeighbors; // used to find the direction of the ship when we only have hit 1 cell
	private boolean shipIsHorizontal;
	private final CustomGridCell[] shipEdges; // keep the top OR left edge at the [0] element and the bottom or right edge at the [1] element
	
	public CPU_AI()
	{
		random = new Random();
		state = State.HAVE_NOT_FOUND_SHIP;
		shipEdges = new CustomGridCell[2];
	}
	
	public void playNextTurn(CustomGridCell[][] playerGrid)
	{
		Task<Void> sleeper = new Task<>()
		{
			protected Void call()
			{
				try { Thread.sleep(1000); } catch(InterruptedException ignored) { }
				return null;
			}
		};
		
		sleeper.setOnSucceeded(event ->
		{
			if(state == State.HAVE_NOT_FOUND_SHIP)
			{
				int xi, yi;
				
				do
				{
					xi = random.nextInt(10);
					yi = random.nextInt(10);
				}
				while(!playerGrid[xi][yi].canShoot());
				
				ShotResult result = playerGrid[xi][yi].shotFired();
				if(result == ShotResult.SHIP_HIT)
				{
					state = State.SHIP_HIT_FIRST_TIME;
					horizontalCellNeighbors = playerGrid[xi][yi].getHorizontalNeighborsThatCanBeHit();
					verticalCellNeighbors = playerGrid[xi][yi].getVerticalNeighborsThatCanBeHit();
					shipEdges[0] = playerGrid[xi][yi];
					shipEdges[1] = playerGrid[xi][yi];
				}
			}
			else if(state == State.SHIP_HIT_FIRST_TIME)
			{
				CustomGridCell nextTarget;
				boolean horizontalDirection;
				
				if(horizontalCellNeighbors.size() == verticalCellNeighbors.size())
				{
					horizontalDirection = random.nextBoolean();
					int index = random.nextInt(horizontalCellNeighbors.size());
					nextTarget = horizontalDirection ? horizontalCellNeighbors.remove(index) : verticalCellNeighbors.remove(index);
				}
				else if(horizontalCellNeighbors.size() > verticalCellNeighbors.size())
				{
					horizontalDirection = true;
					int index = random.nextInt(horizontalCellNeighbors.size());
					nextTarget = horizontalCellNeighbors.remove(index);
				}
				else
				{
					horizontalDirection = false;
					int index = random.nextInt(verticalCellNeighbors.size());
					nextTarget = verticalCellNeighbors.remove(index);
				}
				
				ShotResult result = nextTarget.shotFired();
				if(result == ShotResult.SHIP_HIT)
				{
					horizontalCellNeighbors = null;
					verticalCellNeighbors = null;
					
					state = State.SHIP_HIT_SECOND_TIME;
					shipIsHorizontal = horizontalDirection;
					if(shipIsHorizontal)
					{
						if(nextTarget.getPosition().getY() < shipEdges[0].getPosition().getY())
							shipEdges[0] = nextTarget;
						else
							shipEdges[1] = nextTarget;
					}
					else
					{
						if(nextTarget.getPosition().getX() < shipEdges[0].getPosition().getX())
							shipEdges[0] = nextTarget;
						else
							shipEdges[1] = nextTarget;
					}
				}
				else if(result == ShotResult.SHIP_SHANK)
				{
					horizontalCellNeighbors = null;
					verticalCellNeighbors = null;
					shipEdges[0] = null;
					shipEdges[1] = null;
					
					state = State.HAVE_NOT_FOUND_SHIP;
				}
			}
			else
			{
				List<CustomGridCell> possibleTargets = new ArrayList<>();
				CustomGridCell cell1, cell2;
				
				if(shipIsHorizontal)
				{
					cell1 = playerGrid[shipEdges[0].getPosition().getX()][Math.max(0, shipEdges[0].getPosition().getY() - 1)];
					cell2 = playerGrid[shipEdges[1].getPosition().getX()][Math.min(shipEdges[1].getPosition().getY() + 1, 9)];
				}
				else
				{
					cell1 = playerGrid[Math.max(0, shipEdges[0].getPosition().getX() - 1)][shipEdges[0].getPosition().getY()];
					cell2 = playerGrid[Math.min(shipEdges[1].getPosition().getX() + 1, 9)][shipEdges[1].getPosition().getY()];
				}
				if(cell1.canShoot())
					possibleTargets.add(cell1);
				if(cell2.canShoot())
					possibleTargets.add(cell2);
				
				CustomGridCell nextTarget = possibleTargets.get(random.nextInt(possibleTargets.size()));
				ShotResult result = nextTarget.shotFired();
				if(result == ShotResult.SHIP_HIT)
				{
					if(nextTarget == cell1)
						shipEdges[0] = nextTarget;
					else
						shipEdges[1] = nextTarget;
				}
				else if(result == ShotResult.SHIP_SHANK)
				{
					shipEdges[0] = null;
					shipEdges[1] = null;
					
					state = State.HAVE_NOT_FOUND_SHIP;
				}
			}
			
			Main.updateStatsLabels(false);
			Main.playerTurn();
		});
		new Thread(sleeper).start();
	}
}
