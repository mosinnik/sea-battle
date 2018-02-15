package commands.gameCommands;

import DAO.MemoryDAO;
import commands.Command;
import commands.NewPlayer;
import core.Game;
import core.Input;
import core.arrays.BooleanArray;
import core.arrays.CoordinateArray;
import core.characters.AI;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 23.07.2010
 * Time: 21:57:56
 * To change this template use File | Settings | File Templates.
 */
public class HumanVsAIGame implements Command
{
	public void execute()
	{
//inter and check 'name'
		String name;
		long playerId;
		while(true)
		{
			name=Input.inputName();

			if(name.toLowerCase().equals("exit"))
				return;
			else if(name.toLowerCase().equals("help") || name.toLowerCase().equals("-h") || name.toLowerCase().equals("/?"))
				System.out.println("\n\n\n\n\nHelp.\nYou wil play with AI. For start game you should inter your name. If you aren't register you should do it.");

			playerId=MemoryDAO.getInstance().getPlayerId(name);
			if(playerId==-1)
			{
				System.out.print("Player with such name don't create. Create it? ");
				if(Input.yesOrNo())
					playerId=NewPlayer.newPlayer(name);
				else
					continue;
			}
			break;
		}
//create AI
		long AIId=MemoryDAO.getInstance().newAI(name+"AI", 10);
//input mapSize
		int mapSize=Input.inputMapSize();
//start game
		long gameId=MemoryDAO.getInstance().newGame(playerId, AIId, mapSize);

		Game g=MemoryDAO.getInstance().getGame(gameId);
		AI ai=(AI)MemoryDAO.getInstance().getPlayer(AIId);
		ai.setGame(g);
		g.printGame(true,false);
//add ships by player to sea
		System.out.println("\n"+name+" input coordinates for ship:");
		int n=g.getCountShip();
		CoordinateArray coordinates=new CoordinateArray(-1, -1, false);
		for(int length=n;length>0;length--)
			for(int x=n;x>=length;x--)
			{
				System.out.println("Input "+(n-length+1)+" ship(s) with length="+length);
				coordinates=Input.inputShipCoordinates(g.getMapSize(), g.getCountShip());
				if(coordinates.getI()==-1)
				{   //if -1 then exit
					System.out.println("EXIT");
					return;
				}
				else if(coordinates.getI()==-2)
				{   //if -2 then print help
					System.out.println("Help...");
					x++;
				}
				else
				{
					if(g.checkShipCoordinates(coordinates, length, playerId))
					{
						g.setShip(coordinates, length, playerId);
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
		BooleanArray b=null;
		coordinates=new CoordinateArray(-1, -1);
		while(g.getState())
		{
			if(g.getStep())
			{
//get from first player(human) coordinates for fire
				System.out.print("\n"+name);
				coordinates=Input.inputFireCoordinates(g.getMapSize());
				if(coordinates.getI()==-1)
				{
					System.out.println("Exit");
					MemoryDAO.getInstance().setAI(ai);
					MemoryDAO.getInstance().setGame(g);
					return;
				}
				b=g.fire(coordinates.getI(), coordinates.getK(), playerId);
				if(!b.isShotSuccessful())
					g.changeStep();
				else
					MemoryDAO.getInstance().getPlayer(playerId).addScore(1);
				if(b.isShipDown())
				{
					System.out.println("=============== This ship is down! ==============");
					MemoryDAO.getInstance().getPlayer(playerId).addScore(3);
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
		if(g.getWin())
			System.out.println(" You win!");
		else
			System.out.println(" AI win!");
	}
}
