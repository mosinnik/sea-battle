package commands.continueGameCommands;

import DAO.MemoryDAO;
import commands.Command;
import commands.NewPlayer;
import core.Game;
import core.Input;
import core.arrays.BooleanArray;
import core.arrays.CoordinateArray;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 23.07.2010
 * Time: 21:57:35
 * To change this template use File | Settings | File Templates.
 */
public class ContinueHumanVsHumanGame implements Command
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
			System.out.println("\n\n\nInter first player name:");
			firstPlayerName=Input.inputName();
			if(firstPlayerName.toLowerCase().equals("exit"))
				return;
			else if(firstPlayerName.toLowerCase().equals("help") || firstPlayerName.toLowerCase().equals("-h") || firstPlayerName.toLowerCase().equals("/?"))
			{
				System.out.println("\n\n\n\n\nHelp.\nYou will play with other player. For start game you should inter your name. If you aren't register you should do it.");
				continue;
			}
			firstPlayerId=MemoryDAO.getInstance().getPlayerId(firstPlayerName);
			if(firstPlayerId==-1)
			{
				System.out.println("Error: player with this name don't create. Check name or reload DB.");
				continue;
			}
			break;
		}

//inter and check second player name
		while(true)
		{
			System.out.println("\n\n\nSecond player inter name:");
			secondPlayerName=Input.inputName();
			if(secondPlayerName.toLowerCase().equals("exit"))
				return;
			else if(secondPlayerName.toLowerCase().equals("help") || secondPlayerName.toLowerCase().equals("-h") || secondPlayerName.toLowerCase().equals("/?"))
			{
				System.out.println("\n\n\n\n\nHelp.\nYou will play with AI. For start game you should inter your name. If you aren't register you should do it.");
				continue;
			}
			secondPlayerId=MemoryDAO.getInstance().getPlayerId(secondPlayerName);
			if(secondPlayerId==-1)
			{
				System.out.println("Error: player with this name don't create. Check name or reload DB.");
				continue;
			}
			if(secondPlayerId==firstPlayerId)
			{
				System.out.print("This name already use first player. Input your name again. ");
				continue;
			}
			break;
		}
		System.out.println("\n\n\n");

		HashMap<Long, Game> games=MemoryDAO.getInstance().getGamesHashMap();
		HashMap<Long, Game> checkedGames=new HashMap<Long, Game>();
		for(Game g : games.values())
			if(!g.isSecondPlayerAI() && g.getState())
				if(g.isPlayersIds(firstPlayerId, secondPlayerId))
				{
					checkedGames.put(g.getId(), g);
					System.out.println("\n=====================================================");
					System.out.println("Game id:"+g.getId()+"\tMap size:"+g.getMapSize()+"\tRound:"+g.getRound());
					g.printGame(true, true);
				}
		long gameId=0;
		boolean repeat=true;
		while(repeat)
		{
			System.out.println("Input game id for continue:");
			gameId=Input.inputSelect();
			for(Game g : checkedGames.values())
				if(g.getId()==gameId)
				{
					repeat=false;
					break;
				}
		}

		Game g=MemoryDAO.getInstance().getGame(gameId);
		g.printGame(true, true);

//game
		BooleanArray b=null;
		CoordinateArray coordinates=new CoordinateArray(-1, -1);
		while(g.getState())
		{
			if(g.getStep())
			{
//get from first player(human) coordinates for fire
				System.out.print("\n"+firstPlayerName);
				coordinates=Input.inputFireCoordinates(g.getMapSize());
				if(coordinates.getI()==-1)
				{
					System.out.println("Exit");
					MemoryDAO.getInstance().setGame(g);
					return;
				}
				b=g.fire(coordinates.getI(), coordinates.getK(), firstPlayerId);
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
				System.out.print("\n"+secondPlayerName);
				coordinates=Input.inputFireCoordinates(g.getMapSize());
				if(coordinates.getI()==-1)
				{
					System.out.println("Exit");
					MemoryDAO.getInstance().setGame(g);
					return;
				}
				b=g.fire(coordinates.getI(), coordinates.getK(), secondPlayerId);
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
			System.out.println(firstPlayerName+" win!");
		else
			System.out.println(secondPlayerName+" win!");
	}
}