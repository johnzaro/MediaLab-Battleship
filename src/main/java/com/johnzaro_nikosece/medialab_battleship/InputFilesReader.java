package com.johnzaro_nikosece.medialab_battleship;

import com.johnzaro_nikosece.medialab_battleship.customControls.CustomGridCell;
import com.johnzaro_nikosece.medialab_battleship.dataStructures.ship.*;
import com.johnzaro_nikosece.medialab_battleship.exceptions.*;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InputFilesReader
{
	public static void readInputFiles()
	{
		Main.gameHasStarted = false;
		
		Main.resetGrids();
		
		Main.player.resetUser();
		Main.cpu.resetUser();
		
		boolean playerFileOK, enemyFileOK = false;
		playerFileOK = readInputFile(true);
		if(playerFileOK)
			enemyFileOK = readInputFile(false);
		
		Main.mapIsLoaded = playerFileOK && enemyFileOK;
		
		if(Main.mapIsLoaded)
		{
			Main.updateStatsLabels(true);
			Main.updateStatsLabels(false);
		}
		else
		{
			Main.resetStatsLabels();
		}
	}
	
	private static boolean readInputFile(boolean isPlayersFile)
	{
		try
		{
			List<Ship> currentShipList = isPlayersFile ? Main.player.getShips() : Main.cpu.getShips();
			CustomGridCell[][] currentGridList = isPlayersFile ? Main.player.getCells() : Main.cpu.getCells();
			
			String fileName = (isPlayersFile ? "player" : "enemy") + String.format("_%d.txt", Main.selectedInputID);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Main.class.getResource("/medialab/" + fileName).getFile())));
			
			String line;
			while((line = reader.readLine()) != null)
			{
				String[] elements = line.split(",");
				
				if(elements.length == 4)
				{
					int typeOfShip = Integer.parseInt(elements[0]);
					int x = Integer.parseInt(elements[1]);
					int y = Integer.parseInt(elements[2]);
					boolean isHorizontal = Integer.parseInt(elements[3]) == 1; // horizontal == 1, vertical == 2
					
					Ship ship = switch(typeOfShip)
						            {
							            case 1 -> new Carrier(x, y, isHorizontal);
							            case 2 -> new Battleship(x, y, isHorizontal);
							            case 3 -> new Cruiser(x, y, isHorizontal);
							            case 4 -> new Submarine(x, y, isHorizontal);
							            case 5 -> new Destroyer(x, y, isHorizontal);
							            default -> null;
						            };
					
					if(ship != null)
					{
						for(Ship s : currentShipList)
						{
							if(s.getShipType() == ship.getShipType())
								throw new InvalidCountException();
						}
					}
					
					if(ship != null)
					{
						if(isHorizontal)
						{
							if(x < 0 || x > 9 || y < 0 || y + ship.getShipSize() - 1 > 9)
								throw new OversizeException();
						}
						else
						{
							if(x < 0 || x + ship.getShipSize() - 1 > 9 || y < 0 || y > 9)
								throw new OversizeException();
						}
						
						if(ship.getShipPosition().isHorizontal())
						{
							for(int xi = Math.max(0, ship.getShipPosition().getX() - 1); xi < Math.min(currentGridList.length - 1, ship.getShipPosition().getX() + 1); xi++) // row
							{
								for(int yi = Math.max(0, ship.getShipPosition().getY() - 1); yi < Math.min(currentGridList[yi].length - 1, ship.getShipPosition().getY() + ship.getShipSize() + 1); yi++) // column
								{
									if(! currentGridList[xi][yi].isEmptyCell())
									{
										if(xi == ship.getShipPosition().getX() && yi >= ship.getShipPosition().getY() && yi < ship.getShipPosition().getY() + ship.getShipSize()) throw new OverlapTilesException();
										else throw new AdjacentTilesException();
									}
								}
							}
							
							List<CustomGridCell> usedGridCells = new ArrayList<>();
							
							for(int yi = ship.getShipPosition().getY(); yi < ship.getShipPosition().getY() + ship.getShipSize(); yi++)
							{
								usedGridCells.add(currentGridList[ship.getShipPosition().getX()][yi]);
								currentGridList[ship.getShipPosition().getX()][yi].setShip(ship);
							}
							ship.setUsedGridCells(usedGridCells);
						}
						else
						{
							for(int xi = Math.max(0, ship.getShipPosition().getX() - 1); xi < Math.min(currentGridList.length - 1, ship.getShipPosition().getX() + ship.getShipSize() + 1); xi++) // row
							{
								for(int yi = Math.max(0, ship.getShipPosition().getY() - 1); yi < Math.min(currentGridList[yi].length - 1, ship.getShipPosition().getY() + 1); yi++) // column
								{
									if(! currentGridList[xi][yi].isEmptyCell())
									{
										if(xi >= ship.getShipPosition().getX() && xi < ship.getShipPosition().getX() + ship.getShipSize() && yi == ship.getShipPosition().getY()) throw new OverlapTilesException();
										else throw new AdjacentTilesException();
									}
								}
							}
							
							List<CustomGridCell> usedGridCells = new ArrayList<>();
							
							for(int xi = ship.getShipPosition().getX(); xi < ship.getShipPosition().getX() + ship.getShipSize(); xi++)
							{
								usedGridCells.add(currentGridList[xi][ship.getShipPosition().getY()]);
								currentGridList[xi][ship.getShipPosition().getY()].setShip(ship);
							}
							ship.setUsedGridCells(usedGridCells);
						}
						
						currentShipList.add(ship);
					}
					else throw new WrongFileFormatException();
				}
				else throw new WrongFileFormatException();
			}
			reader.close();
		}
		catch(IOException | NullPointerException e)
		{
			Main.resetGrids();
			showInputErrorAlert((isPlayersFile ? "Player" : "Enemy") + " Input File - FileNotFoundException", "Input File Not Found.");
			return false;
		}
		catch(NumberFormatException | WrongFileFormatException e)
		{
			Main.resetGrids();
			showInputErrorAlert((isPlayersFile ? "Player" : "Enemy") + " Input File - WrongFileFormatException", "There Is An Error In The Input Files.");
			return false;
		}
		catch(InvalidCountException e)
		{
			Main.resetGrids();
			showInputErrorAlert((isPlayersFile ? "Player" : "Enemy") + " Input File - InvalidCountException", "There Can Be No More Than One Ship For Each Type.");
			return false;
		}
		catch(OversizeException e)
		{
			Main.resetGrids();
			showInputErrorAlert((isPlayersFile ? "Player" : "Enemy") + " Input File - OversizeException", "A Ship May Not Exceed The Limits Of The Board Boundaries.");
			return false;
		}
		catch(OverlapTilesException e)
		{
			Main.resetGrids();
			showInputErrorAlert((isPlayersFile ? "Player" : "Enemy") + " Input File - OverlapTilesException", "A Ship Cannot Be Placed In A Cell That Already Has Another Ship.");
			return false;
		}
		catch(AdjacentTilesException e)
		{
			Main.resetGrids();
			showInputErrorAlert((isPlayersFile ? "Player" : "Enemy") + " Input File - AdjacentTilesException", "A Ship May Not Touch Vertically Or Horizontally With Any Other Ship, Even For A Cell.");
			return false;
		}
		
		return true;
	}
	
	private static void showInputErrorAlert(String header, String content)
	{
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error!!!");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
