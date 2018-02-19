package commands.game;

import commands.Command;
import core.Game;
import core.Input;
import core.arrays.BooleanArray;
import core.arrays.CoordinateArray;
import core.characters.Player;
import dao.MemoryDAO;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 23.07.2010
 * Time: 21:57:35
 * To change this template use File | Settings | File Templates.
 */
public class HumanVsHumanGame implements Command
{
	public String helpMessage()
	{
		return "\n\n\n\n\n" +
				"Help.\n" +
				"You will play with another player. " +
				"For start game you should enter your name. " +
				"If you aren't registered you should do it.";
	}


	public void execute()
	{
		Player firstPlayer;
		Player secondPlayer;

		//enter and check players info
		firstPlayer = Input.askForNewPlayerInfo("first player", null, helpMessage());
		if(firstPlayer == null)
			return;
		secondPlayer = Input.askForNewPlayerInfo("second player", firstPlayer, helpMessage());
		if(secondPlayer == null)
			return;

		//enter and check first player name
		System.out.println("\n\n\n");

		//input mapSize
		int mapSize = Input.inputMapSize();

		//start game
		long gameId = MemoryDAO.getInstance().newGame(firstPlayer, secondPlayer, mapSize);
		Game g = MemoryDAO.getInstance().getGame(gameId);
		g.printGame(true, true);

		//add ships by first player to sea
		System.out.println("\n\n\n" + firstPlayer.getName() + " input coordinates for ship:");
		int n = g.getCountShip();
		CoordinateArray coordinates = new CoordinateArray(-1, -1, false);
		for(int length = n; length > 0; length--)
			for(int x = n; x >= length; x--)
			{
				System.out.println("\n" + firstPlayer.getName() + " input " + (n - length + 1) + " ship(s) with length=" + length);
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
					if(g.checkShipCoordinates(coordinates, length, firstPlayer))
					{
						g.setShip(coordinates, length, firstPlayer);
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
		System.out.println("\n\n\n" + secondPlayer.getName() + " input coordinates for ship:");
		for(int length = n; length > 0; length--)
			for(int x = n; x >= length; x--)
			{
				System.out.println("\n" + secondPlayer.getName() + " input " + (n - length + 1) + " ship(s) with length=" + length);
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
					if(g.checkShipCoordinates(coordinates, length, secondPlayer))
					{
						g.setShip(coordinates, length, secondPlayer);
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
				System.out.print("\n" + firstPlayer.getName());
				coordinates = Input.inputFireCoordinates(g.getMapSize());
				if(coordinates.getI() == -1)
				{
					System.out.println("Exit");
					MemoryDAO.getInstance().setGame(g);
					return;
				}
				b = g.fire(coordinates.getI(), coordinates.getK(), firstPlayer);
				if(!b.isShotSuccessful())
					g.changeStep();
				else
					firstPlayer.addScore(1);
				if(b.isShipDown())
				{
					System.out.println("=============== This ship is down! ==============");
					firstPlayer.addScore(3);
				}
			}
			else
			{
				//get from second player(human) coordinates for fire
				System.out.print("\n" + secondPlayer.getName());
				coordinates = Input.inputFireCoordinates(g.getMapSize());
				if(coordinates.getI() == -1)
				{
					System.out.println("Exit");
					MemoryDAO.getInstance().setGame(g);
					return;
				}
				b = g.fire(coordinates.getI(), coordinates.getK(), secondPlayer);
				if(!b.isShotSuccessful())
					g.changeStep();
				else
					secondPlayer.addScore(1);
				if(b.isShipDown())
				{
					System.out.println("=============== This ship is down! ==============");
					secondPlayer.addScore(3);
				}
			}
			System.out.print("\n");
			g.printGame(true, true);
		}
		System.out.print("\n\n\n\n\t\tGame over. ");
		if(g.getWin())
			System.out.println(firstPlayer.getName() + " win!");
		else
			System.out.println(secondPlayer.getName() + " win!");
	}
}
 