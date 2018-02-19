package core;

import commands.NewPlayer;
import core.arrays.CoordinateArray;
import core.characters.Player;
import dao.MemoryDAO;

import java.util.Scanner;
import java.util.StringTokenizer;

public class Input
{
	//todo: add all strings to constants
	final static String ERROR_YES_OR_NO = "Error: need 1 char 'y' or 'n'. Repeat input.";
	final static String YES_OR_NO = "Yes or no?[y/n]";
	final static String INPUT_NAME = "Input name";

	public static CoordinateArray inputFireCoordinates(int mapSize)
	{
		CoordinateArray coordinates = new CoordinateArray(-1, -1);
		System.out.println(" input line[0;" + mapSize + "] and column[0;" + mapSize + "] for shot");
		Scanner in = new Scanner(System.in);
		String st = in.nextLine();

		StringTokenizer strTok = new StringTokenizer(st, " ,.");

		if(strTok.countTokens() != 2)
		{
			if(strTok.hasMoreTokens() && strTok.nextToken().toLowerCase().equals("exit"))
				return coordinates;
			else
			{
				System.out.println("Need input 2 coordinates. Repeat input.");
				return inputFireCoordinates(mapSize);
			}
		}
		else
		{
//check first coordinate [i]
			try
			{
				if(strTok.hasMoreTokens())
				{
					int i = Integer.parseInt(strTok.nextToken());
					if(!(i >= 0 && i < mapSize))
					{
						System.out.println("First coordinate outside of range [0;" + (mapSize - 1) + "]. Repeat input.");
						return inputFireCoordinates(mapSize);
					}
					coordinates.setI(i);
				}
//check second coordinate [k]
				if(strTok.hasMoreTokens())
				{
					int k = Integer.parseInt(strTok.nextToken());
					if(!(k >= 0 && k < mapSize))
					{
						System.out.println("Second coordinate outside of range [0;" + (mapSize - 1) + "]. Repeat input.");
						return inputFireCoordinates(mapSize);
					}
					coordinates.setK(k);
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("Incorrect input data. Repeat input.");
				return inputFireCoordinates(mapSize);
			}
		}
		System.out.println(coordinates.getI() + "  " + coordinates.getK());
		return coordinates;
	}

	public static CoordinateArray inputShipCoordinates(int mapSize, int maxLength)
	{
		CoordinateArray coordinates = new CoordinateArray(-1, -1, false);
		System.out.println("Input line[0;" + mapSize + "],column[0;" + mapSize + "] and direction[h/v] for ship.");
		Scanner in = new Scanner(System.in);
		String st = in.nextLine();

		StringTokenizer strTok = new StringTokenizer(st, " ,");
		//System.out.println("strTok.countTokens()="+strTok.countTokens());
		if(strTok.countTokens() != 3)
		{
			if(strTok.hasMoreTokens())
			{
				st = strTok.nextToken().toLowerCase();
				if(isExitCommand(st))
				{
					return coordinates;
				}
				if(isHelpCommand(st))
				{
					coordinates.setI(-2);
					return coordinates;
				}
			}
			System.out.println("Need input 3 coordinates. Repeat input.");
			return inputShipCoordinates(mapSize, maxLength);
		}
//check first coordinate [i]
		try
		{
			if(strTok.hasMoreTokens())
			{
				int i = Integer.parseInt(strTok.nextToken());
				if(!(i >= 0 && i < mapSize))
				{
					System.out.println("First coordinate outside of range [0;" + (mapSize - 1) + "]. Repeat input.");
					return inputShipCoordinates(mapSize, maxLength);
				}
				coordinates.setI(i);
			}
//check second coordinate [k]
			if(strTok.hasMoreTokens())
			{
				int k = Integer.parseInt(strTok.nextToken());
				if(!(k >= 0 && k < mapSize))
				{
					System.out.println("Second coordinate outside of range [0;" + (mapSize - 1) + "]. Repeat input.");
					return inputShipCoordinates(mapSize, maxLength);
				}
				coordinates.setK(k);
			}
//check direction
			if(strTok.hasMoreTokens())
			{
				st = strTok.nextToken();
				switch(st)
				{
					case "v":
						coordinates.setDirection(MemoryDAO.VERTICAL);
						break;
					case "h":
						coordinates.setDirection(MemoryDAO.HORIZONTAL);
						break;
					default:
						System.out.println("Direction should be 'h' or 'v'. Repeat input.");
						return inputShipCoordinates(mapSize, maxLength);
				}
			}
		}
		catch(NumberFormatException e)
		{
			System.out.println("Incorrect input data. Repeat input.");
			return inputShipCoordinates(mapSize, maxLength);
		}

		System.out.println(coordinates.getI() + "  " + coordinates.getK() + "  " + coordinates.getDirection());
		return coordinates;
	}

	public static int inputMapSize()
	{
		int mapSize = -1;
		System.out.println("Input map size.");
		Scanner in = new Scanner(System.in);
		String st = in.nextLine();

		StringTokenizer strTok = new StringTokenizer(st, " ,");

		try
		{
			if(strTok.hasMoreTokens())
			{
				mapSize = Integer.parseInt(strTok.nextToken());
				if(mapSize <= 0)
				{
					System.out.println("Size should be above 0. Repeat input.");
					return inputMapSize();
				}
			}
		}
		catch(NumberFormatException e)
		{
			System.out.println("Incorrect input data. Repeat input.");
			return inputMapSize();
		}
		return mapSize;
	}

	public static String inputName()
	{
		System.out.println(INPUT_NAME);
		String name = inputLine();
		return name.trim();
	}

	public static String inputLine()
	{
		Scanner in = new Scanner(System.in);
		return in.nextLine();
	}

	public static int inputSelect() throws NumberFormatException
	{
		Scanner in = new Scanner(System.in);
		String st = in.nextLine();
		return Integer.parseInt(st);
	}

	public static boolean yesOrNo()
	{
		System.out.println(YES_OR_NO);
		Scanner in = new Scanner(System.in);
		String st = in.nextLine();
		st = st.toLowerCase();
		if(st.length() != 1)
		{
			System.out.println(ERROR_YES_OR_NO);
			return yesOrNo();
		}
		switch(st)
		{
			case "y":
				return true;
			case "n":
				return false;
			default:
				System.out.println(ERROR_YES_OR_NO);
				return yesOrNo();
		}
	}

	public static boolean isExitCommand(String command)
	{
		if(command == null)
			return false;
		return command.toLowerCase().equals("exit");
	}

	public static boolean isStopCommand(String command)
	{
		if(command == null)
			return false;
		return command.toLowerCase().equals("stop");
	}

	public static boolean isHelpCommand(String command)
	{
		if(command == null)
			return false;

		command = command.toLowerCase();
		switch(command)
		{
			case "help":
			case "-h":
			case "/?":
				return true;
			default:
				return false;
		}
	}

	public static Player askForNewPlayerInfo(String title, Player enemyPlayer, String helpMessage)
	{
		while(true)
		{
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("Enter " + title + " name:");
			String playerName = inputName();
			if(isExitCommand(playerName))
				break;

			if(isHelpCommand(playerName))
			{
				System.out.println(helpMessage);
				continue;
			}

			long playerId = MemoryDAO.getInstance().getPlayerId(playerName);
			if(playerId == -1)
			{
				System.out.print("Player with same name didn't create yet. Create it? ");
				if(!yesOrNo())
					continue;

				playerId = NewPlayer.newPlayer(playerName);
				return MemoryDAO.getInstance().getPlayer(playerId);
			}

			if(enemyPlayer == null || enemyPlayer.getId() != playerId)
				break;

			System.out.print("This name already use first player. Enter your name again.");
		}

		return null;
	}

	public static Player askForLoadPlayerInfo(String title, Player enemyPlayer, String helpMessage)
	{
		while(true)
		{
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("Enter " + title + " name:");
			String playerName = inputName();
			if(isExitCommand(playerName))
				break;

			if(isHelpCommand(playerName))
			{
				System.out.println(helpMessage);
				continue;
			}

			long playerId = MemoryDAO.getInstance().getPlayerId(playerName);
			if(playerId == -1)
			{
				System.out.println("Error: player with this name not exists. Check name or reload DB.");
				continue;
			}

			if(enemyPlayer != null && enemyPlayer.getId() == playerId)
			{
				System.out.print("This name already use first player. Enter your name again.");
				continue;
			}

			return MemoryDAO.getInstance().getPlayer(playerId);
		}

		return null;
	}
}
