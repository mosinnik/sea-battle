package core;

import DAO.MemoryDAO;
import core.arrays.BooleanArray;
import core.arrays.CoordinateArray;

import java.io.Serializable;

public class Game implements Serializable
{
	public Game(long firstPlayerId, long secondPlayerId, int mapSize)
	{
		map1 = new GameMap(mapSize, firstPlayerId);
		map2 = new GameMap(mapSize, secondPlayerId);
		printMap1 = new int[mapSize][mapSize];
		printMap2 = new int[mapSize][mapSize];

		checkMap1 = new int[mapSize][mapSize];
		checkMap2 = new int[mapSize][mapSize];
		for(int i = 0; i < mapSize; i++)
			for(int k = 0; k < mapSize; k++)
			{
				checkMap1[i][k] = 0;
				checkMap2[i][k] = 0;
			}
		this.firstPlayerId = firstPlayerId;
		this.secondPlayerId = secondPlayerId;
		this.isSecondPlayerAI = MemoryDAO.getInstance().getPlayer(secondPlayerId).isAI();
		this.mapSize = mapSize;
		step = true;
		state = true;
		time = 0;
		winFirst = true;
		round = 0;
		id = gameId;
		Game.incGameId();
	}

	public void setShip(CoordinateArray coordinates, int length, long player)
	{
		if(player == map1.getPlayerId())
		{
			map1.setShip(coordinates, length);
			setShipCheck(coordinates, length, player);
		}
		else if(player == map2.getPlayerId())
		{
			map2.setShip(coordinates, length);
			setShipCheck(coordinates, length, player);
		}
	}

	public Ship getShip(int i, int k, long player)
	{
		if(player == map1.getPlayerId())
			return map2.getShip(i, k);
		else if(player == map2.getPlayerId())
			return map1.getShip(i, k);
		return null;
	}

	public void resetMap(long player)
	{
		if(player == map1.getPlayerId())
			map1.resetMap();
		else if(player == map2.getPlayerId())
			map2.resetMap();
	}

	public BooleanArray fire(int i, int k, long player)
	{
		//b[0]-is fire successful
		//b[1]-is fire kill whole ship (need for AI)
		BooleanArray b = new BooleanArray();
		if(state)
		{
			round++;
			b = fireM(i, k, player);
			if(b.isShotSuccessful())
			{
				if(player == map1.getPlayerId())
				{
					map2.decNumbOfDecks();
					if(map2.getNumbOfDecks() == 0)
					{
						state = false;
						winFirst = true;
					}
					return b;
				}
				else if(player == map2.getPlayerId())
				{
					map1.decNumbOfDecks();
					if(map1.getNumbOfDecks() == 0)
					{
						state = false;
						winFirst = false;
					}
					return b;
				}
			}


		}
		else
		{
			System.out.println("By state:Game is finished. " + (winFirst ? "First" : "Second") + " player win.");
		}
		return b;
	}

	public BooleanArray fireM(int i, int k, long player)
	{

		BooleanArray b = new BooleanArray();
		if(player == map2.getPlayerId())
		{
			if(map1.getShip(i, k) != null)
			{
				if(map1.destroy(i, k) != -1)
				{
					printMap2[i][k] = 2;
					b.shotSuccessful();
					if(map1.getShip(i, k).isDown())
						b.shipDown();
				}
			}
			else
			{
				map1.setSea(i, k);
				printMap2[i][k] = 3;
			}
		}
		else if(player == map1.getPlayerId())
		{
			if(map2.getShip(i, k) != null)
			{
				if(map2.destroy(i, k) != -1)
				{
					printMap1[i][k] = 2;
					b.shotSuccessful();
					if(map2.getShip(i, k).isDown())
						b.shipDown();
				}
			}
			else
			{
				map2.setSea(i, k);
				printMap1[i][k] = 3;
			}
		}
		return b;
	}

	public long[] getPlayersIds()
	{
		long[] l = new long[2];
		l[0] = firstPlayerId;
		l[1] = secondPlayerId;
		return l;
	}

	public int getMapSize()
	{
		return mapSize;
	}

	public long getRound()
	{
		return round;
	}

	public boolean getState()
	{
		return state;
	}

	public long getTime()
	{
		return time;
	}

	public boolean getWin()
	{
		return winFirst;
	}

	public long getId()
	{
		return id;
	}

	public boolean isPlayersIds(long id1, long id2)
	{
		if(firstPlayerId == id1 && secondPlayerId == id2 || firstPlayerId == id2 && secondPlayerId == id1)
			return true;
		return false;
	}

	public int[][] getMap(long id)
	{
		int[][] ik = new int[mapSize][mapSize];
		GameMap map;
		if(id == firstPlayerId)
			map = map1;
		else
			map = map2;

		for(int i = 0; i < mapSize; i++)
			for(int k = 0; k < mapSize; k++)
			{
				Ship sh = map.getShip(i, k);
				if(sh != null)
					if(!sh.isSea())
						if(sh.getState(i, k))
							ik[i][k] = 1;//System.out.print("|X");
						else
							ik[i][k] = 2;//System.out.print("|Z");
					else
						ik[i][k] = 3;//System.out.print("|S");
				else
					ik[i][k] = 0;//System.out.print("|_");
			}
		return ik;
	}

	public int[][] getPrintMap(long id)
	{
		if(id == firstPlayerId)
			return printMap1;
		else
			return printMap2;
	}

	public int[][] getCheckMap(long id)
	{
		if(id == firstPlayerId)
			return checkMap1;
		else
			return checkMap2;
	}

	public int getCountShip()
	{
		int n, sum = 0, p = 0;
		int s = mapSize * mapSize;
		for(n = 1; n <= mapSize; n++)
		{
			sum = 0;
			p = 0;
			for(int i = 1; i <= n; i++)
			{
				p += i * (n - i + 1);
				for(int k = 0; k < i; k++)
					sum += 9 + 3 * k;
			}
			if(sum > s)
				break;
		}

		if((double)p * 100 / s > 25)
			n = n - 1;

		return n;
	}

	public boolean checkShipCoordinates(CoordinateArray coordinates, int length, long player)
	{
		int di = 0, dk = 0;

		int i = coordinates.getI();
		int k = coordinates.getK();
		int[][] map;

		if(player == map1.getPlayerId())
			map = checkMap1;
		else
			map = checkMap2;

		if(coordinates.getDirection())
		{
			if(i + length > mapSize)
				return false;
			di = 1;
		}
		else
		{
			if(k + length > mapSize)
				return false;
			dk = 1;
		}
		for(int x = 0; x < length; x++)
		{
			if(map[i][k] != 0)
				return false;
			i += di;
			k += dk;
		}
		return true;
	}

	void setShipCheck(CoordinateArray coordinates, int length, long player)
	{
		int di = 0;
		int dk = 0;

		int i = coordinates.getI();
		int k = coordinates.getK();
		int[][] map;

		if(player == map1.getPlayerId())
			map = checkMap1;
		else
			map = checkMap2;

		if(coordinates.getDirection())
			di = 1;
		else
			dk = 1;

		for(int x = 0; x < length; x++)
		{
			//   ###################
			//   ######k-1# k #k+1##
			//   ###################
			//   ##i-1# 0 # 1 # 2 ##
			//   ###################
			//   ## i # 3 # 4 # 5 ##
			//   ###################
			//   ##i+1# 6 # 7 # 8 ##
			//   ###################

			if(i != 0)
			{
				if(k != 0)
					map[i - 1][k - 1] = 1;
				if(k != mapSize - 1)
					map[i - 1][k + 1] = 1;
				map[i - 1][k] = 1;
			}
			if(i != mapSize - 1)
			{
				if(k != 0)
					map[i + 1][k - 1] = 1;
				if(k != mapSize - 1)
					map[i + 1][k + 1] = 1;
				map[i + 1][k] = 1;
			}
			if(k != 0)
				map[i][k - 1] = 1;
			if(k != mapSize - 1)
				map[i][k + 1] = 1;
			map[i][k] = 1;

			i += di;
			k += dk;
		}
	}

	private static String cellStateSign(int cellState)
	{
		switch(cellState)
		{
			case 0:
				return "_";
			case 1:
				return "X";
			case 2:
				return "Z";
			case 3:
				return "S";
		}
		throw new RuntimeException("Unknown cell state " + cellState);
	}

	private static final String NEW_LINE = String.format("%n");

	private static String printMaps(int[][] map, int[][] enemyMap)
	{
		StringBuilder sb = new StringBuilder();
		int size = map.length;

		sb.append("   ");
		for(int i = 0; i < size; i++)
			sb.append(" ").append(i);
		sb.append("            ");
		for(int i = 0; i < size; i++)
			sb.append(" ").append(i);
		sb.append("  ").append(NEW_LINE);


		for(int i = 0; i < size; i++)
		{
			sb.append(String.format("%3d ", i));
			for(int k = 0; k < size; k++)
				sb.append("|").append(cellStateSign(map[i][k]));
			sb.append("|");

			sb.append("         ").append(String.format("%3d ", i));
			for(int k = 0; k < size; k++)
				sb.append("|").append(cellStateSign(enemyMap[i][k]));
			sb.append("|").append(NEW_LINE);
		}

		return sb.toString();
	}

	public void printGame(boolean printFirstPlayer, boolean printSecondPlayer)
	{
		int size = getMapSize();
		int[][] map1 = getMap(getPlayersIds()[0]);
		int[][] map2 = getMap(getPlayersIds()[1]);
		int[][] map1m = getPrintMap(getPlayersIds()[0]);
		int[][] map2m = getPrintMap(getPlayersIds()[1]);
		long round = getRound();

		String firstUserName = MemoryDAO.getInstance().getPlayer(getPlayersIds()[0]).getName();
		String secondUserName = MemoryDAO.getInstance().getPlayer(getPlayersIds()[1]).getName();

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("=== %s vs %s === round %d ===",
				firstUserName,
				secondUserName,
				round)
		).append(NEW_LINE);

//first player screen
		if(printFirstPlayer)
			sb.append(printMaps(map1, map1m));

		sb.append("----------------------------------------------------").append(NEW_LINE);

//second player screen
		if(printSecondPlayer)
			sb.append(printMaps(map2, map2m));

		System.out.println(sb.toString());
	}

	public boolean isSecondPlayerAI()
	{
		return isSecondPlayerAI;
	}

	public void setStep(boolean step)
	{
		this.step = step;
	}

	public boolean getStep()
	{
		return step;
	}

	public void changeStep()
	{
//change step between players 
		step = !step;
	}


	//work with static id
	public static long getGameId()
	{
		return gameId;
	}

	public static long getNextGameId()
	{
		gameId++;
		return gameId;
	}

	public static void incGameId()
	{
		gameId++;
	}


	private boolean state;//if false then finished
	private GameMap map1;
	private GameMap map2;
	private int[][] printMap1;
	private int[][] printMap2;

	private int[][] checkMap1;
	private int[][] checkMap2;

	private long firstPlayerId;
	private long secondPlayerId;
	private boolean step;//true if step by first player

	private int mapSize;
	private long round;
	private long time;
	private boolean winFirst;

	private long id;
	private static long gameId;
	private boolean isSecondPlayerAI;
}
