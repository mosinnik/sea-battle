package commands.continuegame;

import commands.Command;
import core.Game;
import core.Input;
import core.arrays.BooleanArray;
import core.arrays.CoordinateArray;
import core.characters.AI;
import core.characters.Player;
import dao.MemoryDAO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 23.07.2010
 * Time: 21:57:56
 * To change this template use File | Settings | File Templates.
 */
public class ContinueHumanVsAIGame implements Command
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
		//inter and check player info
		Player player = Input.askForLoadPlayerInfo("Your", null, helpMessage());
		if(player == null)
			return;

		Map<Long, Game> games = MemoryDAO.getInstance().getGamesHashMap();
		Map<Long, Game> checkedGames = new HashMap<Long, Game>();
		System.out.println("Game id\tMap size\tRound");
		for(Game g : games.values())
			if(g.isSecondPlayerAI() && g.getState())
				if(g.getPlayersIds()[0] == player.getId())
				{
					checkedGames.put(g.getId(), g);
					System.out.println("\n=====================================================");
					System.out.println("Game id:" + g.getId() + "\tMap size:" + g.getMapSize() + "\tRound:" + g.getRound());
					g.printGame(true, false);
				}
		long gameId = 0;
		boolean repeat = true;
		while(repeat)
		{
			System.out.println("Input game id for continue:");
			gameId = Input.inputSelect();
			for(Game g : checkedGames.values())
				if(g.getId() == gameId)
				{
					repeat = false;
					break;
				}
		}

		Game g = MemoryDAO.getInstance().getGame(gameId);
		long AIId = g.getPlayersIds()[1];
		AI ai = (AI)MemoryDAO.getInstance().getPlayer(AIId);

		g.printGame(true, false);
//game
		BooleanArray b = null;
		CoordinateArray coordinates = new CoordinateArray(-1, -1);
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
			g.printGame(true, false);
		}
		System.out.print("\n\n\n\n\t\tGame over.");
		if(g.getWin())
			System.out.println(" You win!");
		else
			System.out.println(" AI win!");
	}
}