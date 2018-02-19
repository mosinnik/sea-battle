package commands.game;

import commands.Command;
import core.Game;
import core.Input;
import core.characters.AI;
import dao.MemoryDAO;

/**
 * Created by IntelliJ IDEA.
 * User: mosinnik
 * Date: 26.07.2010
 * Time: 11:26:29
 * To change this template use File | Settings | File Templates.
 */
public class AIVsAIGame implements Command
{
	private AI firstAI = null;
	private AI secondAI = null;
	private Game g = null;
	private long firstAIId;
	private long secondAIId;
	private long gameId;

	public void execute()
	{
//input mapSize
		int mapSize = Input.inputMapSize();

//start game between AIs
		firstAIId = MemoryDAO.getInstance().newAI("firstAI", 10);
		secondAIId = MemoryDAO.getInstance().newAI("secondAI", 10);
		firstAI = (AI)MemoryDAO.getInstance().getPlayer(firstAIId);
		secondAI = (AI)MemoryDAO.getInstance().getPlayer(secondAIId);
		gameId = MemoryDAO.getInstance().newGame(firstAI, secondAI, mapSize);
		g = MemoryDAO.getInstance().getGame(gameId);
		firstAI.setGame(g);
		secondAI.setGame(g);
		AIRunnable AIR = new AIRunnable(firstAI, secondAI, g);
		Thread gameThread = new Thread(AIR);
		gameThread.start();

//wait input some words
		while(true)
		{
			String str = Input.inputLine();
			if(Input.isExitCommand(str) || Input.isStopCommand(str))
			{
				gameThread.interrupt();
				gameThread.stop();
				MemoryDAO.getInstance().setAI(firstAI);
				MemoryDAO.getInstance().setAI(secondAI);
				MemoryDAO.getInstance().setGame(g);
				return;
			}
			else if(Input.isHelpCommand(str))
			{
				gameThread.suspend();
				System.out.println("\n\n\n\t!!!PAUSE!!!!\n\nHelp.\nYou look for game between AIs. You can stop it and continue if need but you have only 1 second for input.[input 'stop','pause','play']");
				System.out.println("For continues game write 'play'");
			}
			else if(str.toLowerCase().equals("pause"))
			{
				gameThread.suspend();
			}
			else if(str.toLowerCase().equals("play"))
			{
				gameThread.resume();
			}
			//break;
		}
	}
}

class AIRunnable implements Runnable
{
	private AI firstAI = null;
	private AI secondAI = null;
	private Game g = null;


	public AIRunnable(AI firstAI, AI secondAI, Game g)
	{
		this.firstAI = firstAI;
		this.secondAI = secondAI;
		this.g = g;
	}

	public AI[] getAI()
	{
		AI[] ais = {this.firstAI, this.secondAI};
		return ais;
	}

	public void run()
	{
		while(g.getState())
		{
			if(g.getStep())
			{//step by firstAI
				if(!firstAI.nextStep())
					g.changeStep();
			}
			else
			{//step by secondAI
				if(!secondAI.nextStep())
					g.changeStep();
			}
			System.out.print("\n\n");
			g.printGame(true, true);
			//between 2 steps 1 second
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		System.out.print("\n\n\n\n\t\tGame over.");
		if(g.getWinner() == firstAI)
			System.out.println(" First AI win!");
		else
			System.out.println(" Second AI win!");
	}
}

