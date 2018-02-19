package commands.game;

import commands.Command;
import core.Game;
import core.Input;
import core.arrays.BooleanArray;
import core.arrays.CoordinateArray;
import core.characters.AI;
import core.characters.Player;
import dao.MemoryDAO;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 23.07.2010
 * Time: 21:57:56
 * To change this template use File | Settings | File Templates.
 */
public class HumanVsAIGame implements Command
{
	public String helpMessage()
	{
		return "\n\n\n\n\n" +
				"Help.\n" +
				"You wil play with AI. " +
				"For start game you should inter your name. " +
				"If you aren't register you should do it.";
	}


	public void execute()
	{
		//enter player info
		Player player = Input.askForNewPlayerInfo("your", null, helpMessage());
		if(player == null)
			return;

		//create AI
		long aiId = MemoryDAO.getInstance().newAI(player.getName() + "AI", 10);
		AI ai = (AI)MemoryDAO.getInstance().getPlayer(aiId);
		//input mapSize
		int mapSize = Input.inputMapSize();
		//start game
		long gameId = MemoryDAO.getInstance().newGame(player, ai, mapSize);

		Game g = MemoryDAO.getInstance().getGame(gameId);
		ai.setGame(g);
		g.printGame(true, false);
		//add ships by player to sea
		System.out.println("\n" + player.getName() + " input coordinates for ship:");
		int n = g.getCountShip();
		CoordinateArray coordinates = new CoordinateArray(-1, -1, false);
		for(int length = n; length > 0; length--)
			for(int x = n; x >= length; x--)
			{
				System.out.println("Input " + (n - length + 1) + " ship(s) with length=" + length);
				coordinates = Input.inputShipCoordinates(g.getMapSize());
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
					if(g.checkShipCoordinates(coordinates, length, player))
					{
						g.setShip(coordinates, length, player);
					}
					else
					{
						System.out.println("\n\n\nCheck is false. Repeat");
						x++;
					}
				}
				System.out.println("\n");
				g.printGame(true, false);
			}

		//game
		BooleanArray b = null;
		coordinates = new CoordinateArray(-1, -1);
		while(g.getState())
		{
			if(g.getStep())
			{
				//get from first player(human) coordinates for fire
				System.out.print("\n" + player.getName());
				coordinates = Input.inputFireCoordinates(g.getMapSize());
				if(coordinates.getI() == -1)
				{
					System.out.println("Exit");
					MemoryDAO.getInstance().setAI(ai);
					MemoryDAO.getInstance().setGame(g);
					return;
				}
				b = g.fire(coordinates.getI(), coordinates.getK(), player);
				if(!b.isShotSuccessful())
					g.changeStep();
				else
					player.addScore(1);
				if(b.isShipDown())
				{
					System.out.println("=============== This ship is down! ==============");
					player.addScore(3);
				}
			}
			else
			{//AI step
				if(!ai.nextStep())
					g.changeStep();
			}
			System.out.print("\n\n");
			g.printGame(true, false); //invisible AI screen
		}
		System.out.print("\n\n\n\n\t\tGame over.");

		if(g.getWinner().isAI())
		{
			System.out.println();
			System.out.println(" AI win!");
			System.out.println();
			System.out.println("AI map:");
			g.printGame(false, true);
		}
		else
			System.out.println(" You win!");
	}
}
