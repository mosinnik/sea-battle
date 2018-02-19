package commands.continuegame;

import commands.Command;
import core.Game;
import core.Input;
import core.arrays.BooleanArray;
import core.arrays.CoordinateArray;
import core.characters.Player;
import dao.MemoryDAO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 23.07.2010
 * Time: 21:57:35
 * To change this template use File | Settings | File Templates.
 */
public class ContinueHumanVsHumanGame implements Command
{
	public String helpMessage()
	{
		return "\n\n\n\n\n" +
				"Help.\n" +
				"You will play with other player. " +
				"For start game you should inter your name. " +
				"If you aren't register you should do it.";
	}


	public void execute()
	{
		Player firstPlayer;
		Player secondPlayer;

		//enter and check players info
		firstPlayer = Input.askForNewPlayerInfo("First", null, helpMessage());
		if(firstPlayer == null)
			return;
		secondPlayer = Input.askForNewPlayerInfo("Second", firstPlayer, helpMessage());
		if(secondPlayer == null)
			return;

		System.out.println("\n\n\n");

		Map<Long, Game> games = MemoryDAO.getInstance().getGamesHashMap();
		Map<Long, Game> checkedGames = new HashMap<Long, Game>();
		for(Game g : games.values())
			if(!g.isSecondPlayerAI() && g.getState())
				if(g.isPlayersIds(firstPlayer.getId(), secondPlayer.getId()))
				{
					checkedGames.put(g.getId(), g);
					System.out.println("\n=====================================================");
					System.out.println("Game id:" + g.getId() + "\tMap size:" + g.getMapSize() + "\tRound:" + g.getRound());
					g.printGame(true, true);
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
		g.printGame(true, true);

//game
		BooleanArray b = null;
		CoordinateArray coordinates = new CoordinateArray(-1, -1);
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
		System.out.println(g.getWinner().getName() + " win!");
	}
}