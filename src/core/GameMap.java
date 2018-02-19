package core;

import core.arrays.CoordinateArray;
import core.characters.Player;

import java.io.Serializable;


public class GameMap implements Serializable
{
	private Player owner;
	public Ship[][] map;
	private int mapSize;
	private int numbOfDecks;

	GameMap(int mapSize, Player owner)
	{
		this.mapSize = mapSize;
		map = new Ship[mapSize][mapSize];
		this.owner = owner;
		numbOfDecks = 0;
	}

	public int getMapSize()
	{
		return mapSize;
	}

	public void setShip(CoordinateArray coordinates, int length)
	{
		int i = coordinates.getI();
		int k = coordinates.getK();

		map[i][k] = new Ship(coordinates, length);
		int dI = 0, dK = 0;
		if(!coordinates.getDirection())
			dK = 1;
		else
			dI = 1;

		for(int ii = 1; ii < length; ii++)
			map[i + dI * ii][k + dK * ii] = map[i][k];

		numbOfDecks += length;
	}

	public void resetMap()
	{
		map = null;
		map = new Ship[mapSize][mapSize];
		numbOfDecks = 0;
	}

	public void setSea(int i, int k)
	{
		map[i][k] = new Ship(true);
	}

	public int destroy(int i, int k)
	{
		return map[i][k].destroy(i, k);
	}

	public Ship getShip(int i, int k)
	{
		if(map[i][k] != null)
			return map[i][k];
		return null;
	}

	public Player getOwner()
	{
		return owner;
	}

	public int getNumbOfDecks()
	{
		return numbOfDecks;
	}

	public void decNumbOfDecks()
	{
		numbOfDecks--;
	}
}
