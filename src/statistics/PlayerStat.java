package statistics;

import core.characters.Player;

public class PlayerStat
{
	private Player player;
	private long inGameTime;
	private int numberWinGame;
	private int numberLoseGame;
	private int numberNotFinishGame;
	private int numberGames;

	public PlayerStat(Player player)
	{
		this.player = player;
	}

	public void addGameTime(long time)
	{
		inGameTime += time;
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

	public String toString()
	{
		return player.getName() + "\t" + player.getScore() + "\t" + numberGames + "\t" + numberWinGame + "\t" + numberLoseGame + "\t" + inGameTime;
	}
}
