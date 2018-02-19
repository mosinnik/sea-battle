package core;

import core.arrays.BooleanArray;
import core.arrays.CoordinateArray;
import core.characters.Player;
import dao.MemoryDAO;

import java.io.Serializable;

public class Game implements Serializable
{
	private boolean state;//if false then finished
	private GameMap map1;
	private GameMap map2;
	private int[][] printMap1;
	private int[][] printMap2;

	private int[][] checkMap1;
	private int[][] checkMap2;

	private Player firstPlayer;
	private Player secondPlayer;
	private boolean step;//true if step by first player

	private int mapSize;
	private long round;
	private long time;
	private Player winner;
	private Player loser;

	private long id;
	private static long gameId;
	private boolean isSecondPlayerAI;

	public Game(Player firstPlayer, Player secondPlayer, int mapSize)
	{
		map1 = new GameMap(mapSize, firstPlayer);
		map2 = new GameMap(mapSize, secondPlayer);
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
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		this.isSecondPlayerAI = secondPlayer.isAI();
		this.mapSize = mapSize;
		step = true;
		state = true;
		time = 0;
		round = 0;
		id = gameId;
		Game.incGameId();
	}

	public void setShip(CoordinateArray coordinates, int length, Player player)
	{
		if(player == map1.getOwner())
		{
			map1.setShip(coordinates, length);
			setShipCheck(coordinates, length, player);
		}
		else if(player == map2.getOwner())
		{
			map2.setShip(coordinates, length);
			setShipCheck(coordinates, length, player);
		}
	}

	public Ship getShip(int i, int k, Player player)
	{
		if(player == map1.getOwner())
			return map2.getShip(i, k);
		else if(player == map2.getOwner())
			return map1.getShip(i, k);
		return null;
	}

	public void resetMap(Player player)
	{
		if(player == map1.getOwner())
			map1.resetMap();
		else if(player == map2.getOwner())
			map2.resetMap();
	}

	public BooleanArray fire(int i, int k, Player player)
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
				if(player == map1.getOwner())
				{
					map2.decNumbOfDecks();
					if(map2.getNumbOfDecks() == 0)
					{
						state = false;
						winner = map1.getOwner();
						loser = map2.getOwner();
					}
					return b;
				}
				else if(player == map2.getOwner())
				{
					map1.decNumbOfDecks();
					if(map1.getNumbOfDecks() == 0)
					{
						state = false;
						winner = map2.getOwner();
						loser = map1.getOwner();
					}
					return b;
				}
			}
		}
		else
		{
			System.out.println("By state: Game is finished.\nPlayer " + winner.getName() + " won.");
		}
		return b;
	}

	public BooleanArray fireM(int i, int k, Player player)
	{

		BooleanArray b = new BooleanArray();
		if(player == map2.getOwner())
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
		else if(player == map1.getOwner())
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
		l[0] = firstPlayer.getId();
		l[1] = secondPlayer.getId();
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

	public Player getWinner()
	{
		return winner;
	}

	public Player getLoser()
	{
		return loser;
	}

	public long getId()
	{
		return id;
	}

	public boolean isPlayersIds(long id1, long id2)
	{
		return firstPlayer.getId() == id1 && secondPlayer.getId() == id2 ||
				firstPlayer.getId() == id2 && secondPlayer.getId() == id1;
	}

	public int[][] getMap(long id)
	{
		int[][] ik = new int[mapSize][mapSize];
		GameMap map;
		if(id == firstPlayer.getId())
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
		if(id == firstPlayer.getId())
			return printMap1;
		else
			return printMap2;
	}

	public int[][] getCheckMap(long id)
	{
		if(id == firstPlayer.getId())
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

	public boolean checkShipCoordinates(CoordinateArray coordinates, int length, Player player)
	{
		int di = 0, dk = 0;

		int i = coordinates.getI();
		int k = coordinates.getK();
		int[][] map;

		if(player == map1.getOwner())
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

	void setShipCheck(CoordinateArray coordinates, int length, Player player)
	{
		int di = 0;
		int dk = 0;

		int i = coordinates.getI();
		int k = coordinates.getK();
		int[][] map;

		if(player == map1.getOwner())
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

	public static void main(String[] args)
	{
		int size = 25;
		StringBuilder sb = new StringBuilder();
		int levelCount = (int)Math.log10(size);
		System.out.println("levelCount = " + Math.log10(size));
		System.out.println("levelCount = " + levelCount);
		for(int level = levelCount; level >= 0; level--)
		{
			addHeaderNumbers(sb, level, size);
			sb.append(NEW_LINE);
		}
		System.out.println(sb.toString());
	}

	private static void addHeaderNumbers(StringBuilder sb, int level, int size)
	{
		double levelDivider = Math.pow(10, level);
		int levelSpaceCount = (int)(levelDivider - 1);
		if(level > 0)
			levelSpaceCount++;
		// fill by spaces
		for(int i = 0; i < levelSpaceCount; i++)
			sb.append("  ");
		// add digits
		for(int i = levelSpaceCount; i < size; i++)
			sb.append(" ").append(((int)(i / levelDivider)) % 10);
		sb.append(" ");
	}

	private static void addMaps(StringBuilder sb, int[][] map, int[][] enemyMap)
	{
		int size = map.length;

		int levelCount = (int)Math.log10(size - 1);
		for(int level = levelCount; level >= 0; level--)
		{
			sb.append("    ");  // indent
			addHeaderNumbers(sb, level, size);
			sb.append("             ");  // indent before enemy map
			addHeaderNumbers(sb, level, size);
			sb.append(NEW_LINE);
		}

		for(int row = 0; row < size; row++)
		{
			sb.append(String.format("%3d ", row));
			for(int column = 0; column < size; column++)
				sb.append("|").append(cellStateSign(map[row][column]));
			sb.append("|");

			sb.append("         ").append(String.format("%3d ", row));
			for(int column = 0; column < size; column++)
				sb.append("|").append(cellStateSign(enemyMap[row][column]));
			sb.append("|");

			sb.append(NEW_LINE);
		}
	}

	private static void addDelimiter(StringBuilder sb, int size)
	{
		int delimiterLength = 0;
		delimiterLength += 4;// for row numbers
		delimiterLength += 2 * size + 1; // for map
		delimiterLength += 9;// intend before enemy map
		delimiterLength += 4;// for row numbers
		delimiterLength += 2 * size + 1; // for map

		for(int i = 0; i < delimiterLength; i++)
			sb.append("-");
		sb.append(NEW_LINE);
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

		// first player screen
		if(printFirstPlayer)
			addMaps(sb, map1, map1m);

		// delimiter
		if(printFirstPlayer && printSecondPlayer)
			addDelimiter(sb, mapSize);

		// second player screen
		if(printSecondPlayer)
			addMaps(sb, map2, map2m);

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

	public Player getFirstPlayer()
	{
		return firstPlayer;
	}

	public Player getSecondPlayer()
	{
		return secondPlayer;
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

}
