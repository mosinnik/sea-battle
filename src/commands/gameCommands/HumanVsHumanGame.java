package commands.gameCommands;

import DAO.MemoryDAO;
import commands.Command;
import commands.NewPlayer;
import core.Game;
import core.Input;
import core.arrays.BooleanArray;
import core.arrays.CoordinateArray;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 23.07.2010
 * Time: 21:57:35
 * To change this template use File | Settings | File Templates.
 */
public class HumanVsHumanGame implements Command
{
	public void execute()
	{
		String firstPlayerName;
		String secondPlayerName;
		long firstPlayerId;
		long secondPlayerId;
//inter and check first player name
		while(true)
		{
			System.out.println("\n\n\nFirst player inter name:");
			firstPlayerName = Input.inputName();
			if(firstPlayerName.toLowerCase().equals("exit"))
				return;
			else if(firstPlayerName.toLowerCase().equals("help") || firstPlayerName.toLowerCase().equals("-h") || firstPlayerName.toLowerCase().equals("/?"))
				System.out.println("\n\n\n\n\nHelp.\nYou will play with other player. For start game you should inter your name. If you aren't register you should do it.");

			firstPlayerId = MemoryDAO.getInstance().getPlayerId(firstPlayerName);
			if(firstPlayerId == -1)
			{
				System.out.print("Player with such name don't create. Create it? ");
				if(Input.yesOrNo())
					firstPlayerId = NewPlayer.newPlayer(firstPlayerName);
				else
					continue;
			}
			break;
		}

//inter and check first player name
		while(true)
		{
			System.out.println("\n\n\nSecond player inter name:");
			secondPlayerName = Input.inputName();
			if(secondPlayerName.toLowerCase().equals("exit"))
				return;
			else if(secondPlayerName.toLowerCase().equals("help") || secondPlayerName.toLowerCase().equals("-h") || secondPlayerName.toLowerCase().equals("/?"))
				System.out.println("\n\n\n\n\nHelp.\nYou will play with AI. For start game you should inter your name. If you aren't register you should do it.");

			secondPlayerId = MemoryDAO.getInstance().getPlayerId(secondPlayerName);
			if(secondPlayerId == -1)
			{
				System.out.print("Player with such name don't create. Create it? ");
				if(Input.yesOrNo())
					secondPlayerId = NewPlayer.newPlayer(secondPlayerName);
				else
					continue;
			}
			if(secondPlayerId == firstPlayerId)
			{
				System.out.print("This name already use first player. Input your name again. ");
				continue;
			}
			break;
		}
		System.out.println("\n\n\n");

//input mapSize
		int mapSize = Input.inputMapSize();

//start game
		long gameId = MemoryDAO.getInstance().newGame(firstPlayerId, secondPlayerId, mapSize);
		Game g = MemoryDAO.getInstance().getGame(gameId);
		g.printGame(true, true);

//add ships by first player to sea
		System.out.println("\n\n\n" + firstPlayerName + " input coordinates for ship:");
		int n = g.getCountShip();
		CoordinateArray coordinates = new CoordinateArray(-1, -1, false);
		for(int length = n; length > 0; length--)
			for(int x = n; x >= length; x--)
			{
				System.out.println("\n" + firstPlayerName + " input " + (n - length + 1) + " ship(s) with length=" + length);
				coordinates = Input.inputShipCoordinates(g.getMapSize(), g.getCountShip());
				if(coordinates.getI() == -1)
				{   //if -1 then exit
					System.out.println("in coord[2]: " + coordinates.getK() + " EXIT");
					x = 0;
					length = 0;
					break;
				}
				else if(coordinates.getI() == -2)
				{   //if -2 then print help
					System.out.println("Help...");
					x++;
				}
				else
				{
					if(g.checkShipCoordinates(coordinates, length, firstPlayerId))
					{
						g.setShip(coordinates, length, firstPlayerId);
					}
					else
					{
						System.out.println("\n\n\nCheck is false. Repeat");
						x++;
					}
				}
				System.out.println("\n\n\n\n");
				g.printGame(true, true);
			}

//add ships by second player to sea
		System.out.println("\n\n\n" + secondPlayerName + " input coordinates for ship:");
		for(int length = n; length > 0; length--)
			for(int x = n; x >= length; x--)
			{
				System.out.println("\n" + secondPlayerName + " input " + (n - length + 1) + " ship(s) with length=" + length);
				coordinates = Input.inputShipCoordinates(g.getMapSize(), g.getCountShip());
				if(coordinates.getI() == -1)
				{   //if -1 then exit
					System.out.println("EXIT");
					return;
				}
				else if(coordinates.getI() == -2)
				{   //if -2 then print help
					System.out.println("Help...");
					x++;
				}
				else
				{
					if(g.checkShipCoordinates(coordinates, length, secondPlayerId))
					{
						g.setShip(coordinates, length, secondPlayerId);
					}
					else
					{
						System.out.println("\n\n\nCheck is false. Repeat");
						x++;
					}
				}
				System.out.println("\n\n");
				g.printGame(true, true);
			}

//game
		BooleanArray b = null;
		coordinates = new CoordinateArray(-1, -1);
		while(g.getState())
		{
			if(g.getStep())
			{
//get from first player(human) coordinates for fire
				System.out.print("\n" + firstPlayerName);
				coordinates = Input.inputFireCoordinates(g.getMapSize());
				if(coordinates.getI() == -1)
				{
					System.out.println("Exit");
					MemoryDAO.getInstance().setGame(g);
					return;
				}
				b = g.fire(coordinates.getI(), coordinates.getK(), firstPlayerId);
				if(!b.isShotSuccessful())
					g.changeStep();
				else
					MemoryDAO.getInstance().getPlayer(firstPlayerId).addScore(1);
				if(b.isShipDown())
				{
					System.out.println("=============== This ship is down! ==============");
					MemoryDAO.getInstance().getPlayer(firstPlayerId).addScore(3);
				}
			}
			else
			{
//get from second player(human) coordinates for fire
				System.out.print("\n" + secondPlayerName);
				coordinates = Input.inputFireCoordinates(g.getMapSize());
				if(coordinates.getI() == -1)
				{
					System.out.println("Exit");
					MemoryDAO.getInstance().setGame(g);
					return;
				}
				b = g.fire(coordinates.getI(), coordinates.getK(), secondPlayerId);
				if(!b.isShotSuccessful())
					g.changeStep();
				else
					MemoryDAO.getInstance().getPlayer(secondPlayerId).addScore(1);
				if(b.isShipDown())
				{
					System.out.println("=============== This ship is down! ==============");
					MemoryDAO.getInstance().getPlayer(secondPlayerId).addScore(3);
				}
			}
			System.out.print("\n");
			g.printGame(true, true);
		}
		System.out.print("\n\n\n\n\t\tGame over. ");
		if(g.getWin())
			System.out.println(firstPlayerName + " win!");
		else
			System.out.println(secondPlayerName + " win!");
	}
}
 