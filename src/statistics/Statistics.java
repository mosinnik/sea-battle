package statistics;

import core.Game;
import core.characters.Player;

import java.util.HashMap;
import java.util.Map;


public class Statistics
{
	private Map<Player, PlayerStat> mPlayersStat;
	private Map<Long, Game> mGames;

	public Statistics(Map<Long, Game> mGames)
	{
		this.mPlayersStat = new HashMap<>();
		this.mGames = mGames;
	}

	private void updateStat(Player player, Game g)
	{
		if(player.isAI())
			return;

		PlayerStat ps = mPlayersStat.computeIfAbsent(player, PlayerStat::new);
		ps.addGameTime(g.getTime());
		ps.addNumberGame();

		if(g.getWinner() == null)
			ps.addNumberNotFinishGame();
		else if(g.getWinner() == player)
			ps.addNumberWinGame();
		else
			ps.addNumberLoseGame();
	}

	public void collectStat()
	{
		for(Game g : mGames.values())
		{
			updateStat(g.getFirstPlayer(), g);
			updateStat(g.getSecondPlayer(), g);
		}
	}

	public void printStatistics()
	{
		System.out.println("Player\tScore\tGames\tWin\tLose\tTime");
		for(PlayerStat ps : mPlayersStat.values())
		{
			System.out.println(ps.toString());
		}
	}
}
