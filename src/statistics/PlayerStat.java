package statistics;

import core.characters.Player;

public class PlayerStat extends Player
{
	public PlayerStat(String name)
	{
		super(name,false);
		inGameTime=0;
		numberWinGame=0;
		numberLoseGame=0;
		numberNotFinishGame=0;
		numberGames=0;
	}

	public void addGameTime(long time)
	{
		inGameTime+=time;
	}

	public void addNumberWinGame()
	{
		numberWinGame++;
	}

	public void addNumberLoseGame()
	{
		numberLoseGame++;
	}

	public void addNumberNotFinishGame()
	{
		numberNotFinishGame++;
	}

	public void addNumberGame()
	{
		numberGames++;
	}

	public long getInGameTime()
	{
		return inGameTime;
	}

	public int getNumberWinGame()
	{
		return numberWinGame;
	}

	public int getNumberLoseGame()
	{
		return numberLoseGame;
	}

	public int getNumberNotFinishGame()
	{
		return numberNotFinishGame;
	}

	public String toString(){
		return getName()+"\t"+getScore()+"\t"+numberGames+"\t"+numberWinGame+"\t"+numberLoseGame+"\t"+inGameTime;
	}

	private long inGameTime;
	private int numberWinGame;
	private int numberLoseGame;
	private int numberNotFinishGame;
	private int numberGames;
}
